package ru.java.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

// Указывает, что данный класс является сущностью
@Entity
// Задает имя таблицы, на которую будет отображаться сущность
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityStudent {

    @Id
    // Стратегия генерации идентификаторов
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Задает имя и некоторые свойства поля таблицы, на которое будет отображаться поле сущности
    @Column(name = "student_name")
    private String name;

    // Указывает на связь между таблицами "один к одному"
    @OneToOne(cascade = CascadeType.ALL)
    // Задает связующий столбец в таблице родительской сущности
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    // Указывает на связь между таблицами "один ко многим"
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "student_id")
    private List<EMail> emails;

    // Указывает на связь между таблицами "многие ко многим"
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // Задает таблицу связей между таблицами для хранения родительской и связанной сущностью
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
}
