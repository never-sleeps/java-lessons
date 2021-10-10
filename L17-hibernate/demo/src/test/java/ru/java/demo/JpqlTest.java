package ru.java.demo;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.java.demo.model.Avatar;
import ru.java.demo.model.Course;
import ru.java.demo.model.EMail;
import ru.java.demo.model.UniversityStudent;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.java.HibernateUtils.buildSessionFactory;
import static ru.java.HibernateUtils.doInSessionWithTransaction;

class JpqlTest {
    private static final int EXPECTED_NUMBER_OF_STUDENTS = 10;

    private static final long FIRST_STUDENT_ID = 1L;
    private static final long FIRST_EMAIL_ID = 1L;

    private static final String FIRST_STUDENT_NAME = "Name1";

    private SessionFactory sessionFactory;

    private static UniversityStudent makeStudentByNumAndId(long id, int num) {
        var avatar = new Avatar(id, String.format("http://avatar%d.ru/", num));
        var eMail = new EMail(id, String.format("any@addr%d.ru", num));
        var course = new Course(id, String.format("Course№%d", num));
        return new UniversityStudent(
                id,
                String.format("Name%d", num),
                avatar,
                List.of(eMail),
                List.of(course)
        );
    }

    @BeforeEach
    void setUp() {
        sessionFactory = buildSessionFactory(UniversityStudent.class, Avatar.class, EMail.class, Course.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        // сохраняем 10 сущностей UniversityStudent
        for (int i = 1; i <= EXPECTED_NUMBER_OF_STUDENTS; i++) {
            var student = makeStudentByNumAndId(0, i);
            doInSessionWithTransaction(sessionFactory, session -> session.persist(student));
        }
        sessionFactory.getStatistics().clear();
    }

    @DisplayName(" должен загружать информацию о нужном студенте по его имени")
    @Test
    void shouldFindExpectedStudentByName() {
        doInSessionWithTransaction(sessionFactory, session -> {

            var query = session.createQuery("select s " +
                    "from UniversityStudent s " +
                    "where s.name = :name", UniversityStudent.class);

            query.setParameter("name", FIRST_STUDENT_NAME);

            var students = query.getResultList();

            assertThat(students).usingRecursiveFieldByFieldElementComparator()
                    .containsOnlyOnce(makeStudentByNumAndId(FIRST_STUDENT_ID, 1));
        });
    }

    @DisplayName(" должен изменять имя заданного студента по его id")
    @Test
    void shouldUpdateStudentNameById() {
        doInSessionWithTransaction(sessionFactory, session -> {
            var firstStudent = session.find(UniversityStudent.class, FIRST_STUDENT_ID);
            var oldName = firstStudent.getName();
            var newName = firstStudent.getName() + firstStudent.getName();

            // необходимо, поскольку запросы на изменение/удаление влияют на БД, но не влияют на контекст
            // (то есть в базе сущности не будет, но в контексте она останется), а find() обращается непосредственно в контекст
            session.detach(firstStudent);

            var query = session.createQuery("update UniversityStudent s " +
                    "set s.name = :name " +
                    "where s.id = :id");
            query.setParameter("id", FIRST_STUDENT_ID);
            query.setParameter("name", newName);
            query.executeUpdate();

            var updatedStudent = session.find(UniversityStudent.class, FIRST_STUDENT_ID);

            assertThat(updatedStudent.getName()).isNotEqualTo(oldName).isEqualTo(newName);
        });
    }

    @DisplayName(" должен удалять заданный email по его id")
    @Test
    void shouldDeleteStudentNameById() {
        doInSessionWithTransaction(sessionFactory, session -> {
            var firstEMail = session.find(EMail.class, FIRST_EMAIL_ID);
            assertThat(firstEMail).isNotNull();

            // необходимо, поскольку запросы на изменение/удаление влияют на БД, но не влияют на контекст
            // (то есть в базе сущности не будет, но в контексте она останется), а find() обращается непосредственно в контекст
            session.detach(firstEMail);

            var query = session.createQuery("delete " +
                    "from EMail e " +
                    "where e.id = :id");
            query.setParameter("id", FIRST_EMAIL_ID);
            query.executeUpdate();

            var deletedEMail = session.find(EMail.class, FIRST_EMAIL_ID);

            assertThat(deletedEMail).isNull();
        });
    }

    @DisplayName("должен загружать список всех студентов с полной информацией о них")
    @Test
    void shouldReturnCorrectStudentsListWithAllInfo() {
        doInSessionWithTransaction(sessionFactory, session -> {

            //EntityGraph<?> entityGraph = session.getEntityGraph("student-avatars-entity-graph");
            var query = session.createQuery("select s from UniversityStudent s", UniversityStudent.class);
            //var query = session.createQuery("select s from UniversityStudent s join fetch s.emails", UniversityStudent.class);
            //query.setHint("javax.persistence.fetchgraph", entityGraph);

            var students = query.getResultList();

            assertThat(students).isNotNull().hasSize(EXPECTED_NUMBER_OF_STUDENTS)
                    .allMatch(s -> !s.getName().equals(""))
                    .allMatch(s -> s.getCourses() != null && s.getCourses().size() > 0)
                    .allMatch(s -> s.getAvatar() != null)
                    .allMatch(s -> s.getEmails() != null && s.getEmails().size() > 0);
        });
        // 31 = 1 (за всеми сущностями)
        // + 10 (avatar для каждой из сущностей student)
        // + 10 (email для каждой из сущностей student)
        // + 10 (course для каждой из сущностей student)
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(31);
    }

    @DisplayName("должен загружать ожидаемый список студентов по номеру страницы")
    @Test
    void shouldReturnCorrectStudentsListByPage() {
        doInSessionWithTransaction(sessionFactory, session -> {
            var studentsCount = session.createQuery(
                    "select count(s) from UniversityStudent s",
                    Long.class
            ).getSingleResult();
            var pageNum = 2;
            var pageSize = 3;
            var pagesCount = (long) Math.ceil(studentsCount * 1d / pageSize);

            var query = session.createQuery("select s from UniversityStudent s ", UniversityStudent.class);
/*
            // Так не будет offset + limit из-за того, что студент может занимать больше одной строки набора данных
            var query = session.createQuery("select s " +
                            "from UniversityStudent s join fetch s.courses c",
                    UniversityStudent.class);
*/
            var students = query.setFirstResult(pageNum * pageSize).setMaxResults(pageSize).getResultList();

            assertThat(pagesCount).isEqualTo(4);
            assertThat(students).isNotNull().hasSize(pageSize);
        });
    }
}