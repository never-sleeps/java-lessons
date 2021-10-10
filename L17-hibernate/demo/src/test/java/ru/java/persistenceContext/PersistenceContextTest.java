package ru.java.persistenceContext;

import org.hibernate.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.java.persistenceContext.model.Avatar;
import ru.java.persistenceContext.model.Student;
import ru.java.persistenceContext.model.Teacher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static ru.java.HibernateUtils.*;

class PersistenceContextTest {
    private Avatar avatar;
    private Student student;

    private SessionFactory sf;

    @BeforeEach
    void setUp() {
        avatar = new Avatar(0, "http://avatar.ru/");
        student = new Student(0, "Ivan Ivanov", avatar);

        sf = buildSessionFactory(Student.class, Teacher.class, Avatar.class);

        sf.getStatistics().setStatisticsEnabled(true);
    }

    @DisplayName("persist не вставляет сущность в БД без транзакции")
    @Test
    void shouldNeverPersistEntityToDBWhenTransactionDoesNotExists() {
        // when
        doInSession(sf, session -> session.persist(student));

        // then
        assertThat(sf.getStatistics().getSessionOpenCount()).isEqualTo(1);
        assertThat(sf.getStatistics().getTransactionCount()).isZero();
        assertThat(sf.getStatistics().getPrepareStatementCount()).isZero();
    }


    @DisplayName("persist вставляет сущность и ее связь в БД при наличии транзакции")
    @Test
    void shouldPersistEntityWithRelationToDBWhenTransactionExists() {
        // when
        // дочерняя сущность avatar также будет успешно сохранена, поскольку для неё в Student включены каскадные операции
        doInSessionWithTransaction(sf, session -> session.persist(student));

        // then
        assertThat(sf.getStatistics().getSessionOpenCount()).isEqualTo(1);
        assertThat(sf.getStatistics().getTransactionCount()).isEqualTo(1);
        assertThat(sf.getStatistics().getFlushCount()).isEqualTo(1);
        assertThat(sf.getStatistics().getEntityInsertCount()).isEqualTo(2);
        assertThat(sf.getStatistics().getPrepareStatementCount()).isEqualTo(2);
    }

    @DisplayName("выкидывает исключение если вставляемая сущность в состоянии detached")
    @Test
    void shouldThrowExceptionWhenPersistDetachedEntity() {
        // сущность с заданным id считается detached, persist возможен только для сущностей со статусом transient
        var avatar = new Avatar(1L, "http://any-addr.ru/");

        // when then
        assertThatCode(() ->
                doInSessionWithTransaction(sf, session -> session.persist(avatar))
        ).hasCauseInstanceOf(PersistentObjectException.class);

        assertThat(sf.getStatistics().getFlushCount()).isZero();
    }


    @DisplayName("persist выкидывает исключение, если вставляемая сущность " +
            "содержит дочернюю в состоянии transient (т.е. она отсутствует в контексте) при выключенной каскадной операции")
    @Test
    void shouldThrowExceptionWhenPersistEntityWithChildInTransientStateAndDisabledCascadeOperation() {
        // дочерняя сущность avatar не может быть сохранена (она в transient),
        // поскольку для неё в Teacher выключены каскадные операции, и эта сущность отсутствует в контексте.
        // Решается помещением avatar в контекст либо же включением для этой сущности каскадных операций в Teacher
        var teacher = new Teacher(0, "Ivan Ivanovich", avatar);

        // when then
        assertThatCode(() ->
                doInSessionWithTransaction(sf, session -> session.persist(teacher))
        ).hasCauseInstanceOf(TransientObjectException.class);

        assertThat(sf.getStatistics().getFlushCount()).isZero();
        assertThat(sf.getStatistics().getPrepareStatementCount()).isEqualTo(1); // call next value for hibernate_sequence
    }

    @DisplayName("persist вставляет сущность несмотря на выключенные каскадные операции для дочерней сущности, " +
            "поскольку её дочерняя сущность уже находится в состоянии persist (уже находится в контексте)")
    @Test
    void shouldPersistEntityWhenPersistEntityWithChildInPersistStateAndDisabledCascadeOperation() {
        // Решается помещением avatar в контекст
        var teacher = new Teacher(0, "Ivan Ivanovich", avatar);

        // when
        doInSessionWithTransaction(sf, session -> {
                    session.persist(avatar);
                    session.persist(teacher);
                }
        );

        // then
        assertThat(sf.getStatistics().getFlushCount()).isEqualTo(1);
        assertThat(sf.getStatistics().getEntityInsertCount()).isEqualTo(2);
        assertThat(sf.getStatistics().getPrepareStatementCount()).isEqualTo(3);
    }


    @DisplayName("изменения в сущности под управлением контекста попадают в БД при закрытии сессии")
    @Test
    void shouldSaveEntityChangesToDBAfterSessionClosing() {
        // given
        var newName = "New Name";

        // when
        doInSessionWithTransaction(sf, session -> {
            session.persist(student);
            student.setName(newName);
        });

        // then
        assertThat(sf.getStatistics().getEntityUpdateCount()).isEqualTo(1);

        doInSessionWithTransaction(sf, session -> {
            var actualStudent = session.find(Student.class, student.getId());
            assertThat(actualStudent.getName()).isEqualTo(newName);
        });
    }

    @DisplayName("изменения в сущности в состоянии detached не попадают в БД при закрытии сессии")
    @Test
    void shouldNotSaveDetachedEntityChangesToDBAfterSessionClosing() {
        // given
        var oldName = student.getName();
        var newName = "New Name";

        // when
        doInSessionWithTransaction(sf, session -> {
            session.persist(student);

            session.detach(student);

            student.setName(newName);
        });

        // then
        assertThat(sf.getStatistics().getEntityUpdateCount()).isZero();

        doInSessionWithTransaction(sf, session -> {
            var actualStudent = session.find(Student.class, student.getId());
            assertThat(actualStudent.getName()).isEqualTo(oldName);
        });
    }

    @DisplayName("изменения в сущности не попадают в БД при закрытии сессии, " +
            "если отключен автоматический flush при закрытии сессии")
    @Test
    void shouldNotSaveEntityChangesToDBAfterSessionClosingForManualFlushMode() {
        // given
        var oldName = student.getName();
        var newName = "New Name";

        // when
        doInSessionWithTransaction(sf, session -> {
            session.persist(student);

            session.setHibernateFlushMode(FlushMode.MANUAL);

            student.setName(newName);
        });

        // then
        assertThat(sf.getStatistics().getEntityUpdateCount()).isZero();

        doInSessionWithTransaction(sf, session -> {
            var actualStudent = session.find(Student.class, student.getId());
            assertThat(actualStudent.getName()).isEqualTo(oldName);
        });
    }


    @DisplayName("merge при сохранении transient сущности работает как persist, " +
            "а при сохранении detached делает дополнительный запрос в БД")
    @Test
    void shouldWorkAsPersistWhenSaveTransientEntityAndDoAdditionalSelectWhenSaveDetachedEntity() {
        // avatar в состоянии transient, проверяем что merge будет работать как persist
        doInSessionWithTransaction(sf, session -> session.merge(avatar));

        assertThat(sf.getStatistics().getEntityInsertCount()).isEqualTo(1); // insert into avatars (id, photo_url) values (null, ?)
        assertThat(sf.getStatistics().getEntityLoadCount()).isEqualTo(0);
        assertThat(sf.getStatistics().getEntityUpdateCount()).isEqualTo(0);
        sf.getStatistics().clear();

        // искусственное присвоение, поскольку на самом деле merge нам уже вернул объект с id,
        // но для красоты кода тот id мы не никак сохраняем
        avatar.setId(1L);
        avatar.setPhotoUrl("http://avatar2.ru/");

        // avatar в состоянии detached (поскольку ему явно задан id),
        // проверяем что merge для detached сущности будет делать дополнительный запрос в БД
        doInSessionWithTransaction(sf, session -> session.merge(avatar));

        // select avatar0_.id as id1_0_0_, avatar0_.photo_url as photo_ur2_0_0_ from avatars avatar0_ where avatar0_.id=?
        assertThat(sf.getStatistics().getEntityLoadCount()).isEqualTo(1);
        assertThat(sf.getStatistics().getEntityUpdateCount()).isEqualTo(1); // update avatars set photo_url=? where id=?
        assertThat(sf.getStatistics().getEntityInsertCount()).isEqualTo(0);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @DisplayName("при доступе к LAZY полю за пределами сессии выкидывается исключение")
    @Test
    void shouldThrowExceptionWhenAccessingToLazyField() {
        // given
        doInSessionWithTransaction(sf, session -> session.persist(student)); // сохраняем в одной сессии

        // when
        Student actualStudent;
        try (var session = sf.openSession()) {
            actualStudent = session.find(Student.class, 1L); // находим сущность в другой сессии
        }

        // then
        // после закрытия сессии при попытке получить значение LAZY поля сущности будет возникать LazyInitializationException
        assertThatCode(() -> actualStudent.getAvatar().getPhotoUrl())
                .isInstanceOf(LazyInitializationException.class);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @DisplayName("при доступе к LAZY полю в пределах сессии значение поля возвращается корректно")
    @Test
    void shouldNotThrowExceptionWhenAccessingToLazyFieldInSession() {
        doInSessionWithTransaction(sf, session -> session.persist(student)); // сохраняем в одной сессии
        sf.getStatistics().clear();

        // when then
        try (var session = sf.openSession()) {
            Student actualStudent = session.find(Student.class, 1L); // находим сущность в другой сессии
            assertThat(actualStudent.getAvatar()).isNotNull().isEqualTo(avatar);
        }
        assertThat(sf.getStatistics().getEntityLoadCount()).isEqualTo(2);
        //     select
        //        student0_.id as id1_1_0_,
        //        student0_.avatar_id as avatar_i3_1_0_,
        //        student0_.student_name as student_2_1_0_
        //    from students student0_
        //    where student0_.id=?

        //     select
        //        avatar0_.id as id1_0_0_,
        //        avatar0_.photo_url as photo_ur2_0_0_
        //    from avatars avatar0_
        //    where avatar0_.id=?
    }
}