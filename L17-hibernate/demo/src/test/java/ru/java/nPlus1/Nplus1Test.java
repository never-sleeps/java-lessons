package ru.java.nPlus1;

import org.hibernate.SessionFactory;
import org.hibernate.graph.RootGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.java.nPlus1.model.Avatar;
import ru.java.nPlus1.model.Course;
import ru.java.nPlus1.model.EMail;
import ru.java.nPlus1.model.Student;


import javax.persistence.EntityGraph;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.java.HibernateUtils.buildSessionFactory;
import static ru.java.HibernateUtils.doInSessionWithTransaction;

class Nplus1Test {

    private static final int EXPECTED_NUMBER_OF_STUDENTS = 10;

    private SessionFactory sessionFactory;

    private static Student makeStudentByNumAndId(long id, int num) {
        var avatar = new Avatar(id, String.format("http://avatar%d.ru/", num));
        var eMail = new EMail(id, String.format("any@addr%d.ru", num));
        var course = new Course(id, String.format("Course№%d", num));
        return new Student(
                id,
                String.format("Name%d", num),
                avatar,
                List.of(eMail),
                List.of(course)
        );
    }

    @BeforeEach
    void setUp() {
        sessionFactory = buildSessionFactory(Student.class, Avatar.class, EMail.class, Course.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        // сохраняем 10 сущностей UniversityStudent
        for (int i = 1; i <= EXPECTED_NUMBER_OF_STUDENTS; i++) {
            var student = makeStudentByNumAndId(0, i);
            doInSessionWithTransaction(sessionFactory, session -> session.persist(student));
        }
        sessionFactory.getStatistics().clear();
    }

    @DisplayName("должен загружать список всех студентов с полной информацией о них")
    @Test
    void shouldReturnCorrectStudentsListWithAllInfo() {
        doInSessionWithTransaction(sessionFactory, session -> {

            // avatars подсоединили к основному запросу через EntityGraph
            // emails - через join fetch в самом query
            // сourses - через @Fetch(FetchMode.SUBSELECT) (все данные таблицы "courses" будут загружены в память отдельным запросом и соединены с родительской сущностью)
            //         - либо через  @BatchSize(size = 5) @Fetch(FetchMode.SELECT) - тогда курсы будут доставаться пачками по 5 штук (сейчас закомментировано в Student.java)


            EntityGraph<?> entityGraph = session.getEntityGraph("student-avatars-entity-graph");
            var query = session.createQuery("select s from Student s left join fetch s.emails", Student.class);
            query.setHint("javax.persistence.fetchgraph", entityGraph);

            var students = query.getResultList();

            assertThat(students).isNotNull().hasSize(EXPECTED_NUMBER_OF_STUDENTS)
                    .allMatch(s -> !s.getName().equals(""))
                    .allMatch(s -> s.getCourses() != null && s.getCourses().size() > 0)
                    .allMatch(s -> s.getAvatar() != null)
                    .allMatch(s -> s.getEmails() != null && s.getEmails().size() > 0);
        });

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(2);
        assertThat(sessionFactory.getStatistics().getEntityLoadCount()).isEqualTo(40);
    }

    @DisplayName("должен загружать список всех студентов с полной информацией о них " +
            "(аналогичный предыдущему пример, но с применением RootGraph из  hibernate)")
    @Test
    void shouldReturnCorrectStudentsListWithAllInfo_RootGraph() {
        doInSessionWithTransaction(sessionFactory, session -> {

            RootGraph<?> entityGraph = session.getEntityGraph("student-avatars-entity-graph");
            var query = session.createQuery("select s from Student s join fetch s.emails", Student.class);
            query.applyFetchGraph(entityGraph);

            var students = query.getResultList();

            assertThat(students).isNotNull().hasSize(EXPECTED_NUMBER_OF_STUDENTS)
                    .allMatch(s -> !s.getName().equals(""))
                    .allMatch(s -> s.getCourses() != null && s.getCourses().size() > 0)
                    .allMatch(s -> s.getAvatar() != null)
                    .allMatch(s -> s.getEmails() != null && s.getEmails().size() > 0);
        });
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(2);
        assertThat(sessionFactory.getStatistics().getEntityLoadCount()).isEqualTo(40);
    }

}