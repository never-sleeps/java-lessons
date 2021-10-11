package ru.java.nPlus1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "student-avatars-entity-graph",
        attributeNodes = {@NamedAttributeNode("avatar")}
)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "student_name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "student_id")
    private List<EMail> emails;


//    @BatchSize(size = 5) // указывает фреймворку, что нужно делать запрос за связями для каждых N записей набора данных родительских сущностей
//    @Fetch(FetchMode.SELECT) // @BatchSize работает для FETCH-режима по умолчанию (@Fetch(FetchMode.SELECT))

    @Fetch(FetchMode.SUBSELECT) // указывает фреймворку, что нужно отдельно поднять список дочерних сущностей из БД, чтобы потом переженить со списком родительских
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
}