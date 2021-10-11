### Проблема N+1 запросов

#### Решение 1. EntityGraph
позволяет указать какие свойства/поля сущности загружать с ней в одном запросе

```java
@Entity
@Table(name = "students")
@NamedEntityGraph(
        name = "student-avatars-entity-graph",
        attributeNodes = {@NamedAttributeNode("avatar")}
)
public class Student {}


@Override
public List<Student> findAll() {
    EntityGraph<?> entityGraph = entityManager.getEntityGraph("student-avatars-entity-graph");
    Query<Student> query = em.createQuery("select s from Student s", Student.class);
    query.setHint("javax.persistence.fetchgraph", entityGraph); 
    return query.getResultList();
}
```
------------

#### Решение 2. JOIN FETCH
позволяет загружать дочерние сущности в одном запросе с родительской

```java
Query<Student> query = entityManager.createQuery("select s from Student s join fetch s.emails ", Student.class);
List<Student> students = query.getResultList();
```
------------

#### Решение 3. @Fetch(SUBSELECT) (аннотация Hibernate)
указывает фреймворку, что нужно отдельно поднять список дочерних сущностей из БД, чтобы потом переженить со списком родительских

```java
@Entity
@Table(name = "students")
public class Student {
    // Все данные таблицы "courses" будут загружены в память отдельным запросом и 
    // соединены с родительской сущностью
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
}
```
------------

#### Решение 4. @BatchSize(size = N) (аннотация Hibernate)
указывает фреймворку, что нужно делать запрос за связями для каждых N записей набора данных родительских сущностей.  
Работает для FETCH-режима по умолчанию (@Fetch(FetchMode.SELECT)).

```java
@Entity
@Table(name = "students")
public class Student {
    @BatchSize(size = 5)
    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
}
```
