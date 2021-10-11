package ru.java.demo;

import ru.java.HibernateUtils;
import ru.java.demo.model.Avatar;
import ru.java.demo.model.Course;
import ru.java.demo.model.EMail;
import ru.java.demo.model.UniversityStudent;

public class Main {
    public static void main(String[] args) {
        HibernateUtils.buildSessionFactory(UniversityStudent.class, Avatar.class, EMail.class, Course.class);
    }
}

/*
    drop table if exists avatars CASCADE
    drop table if exists courses CASCADE
    drop table if exists emails CASCADE
    drop table if exists student_courses CASCADE
    drop table if exists students CASCADE


    create table avatars (
       id bigint generated by default as identity,
        photo_url varchar(255),
        primary key (id)
    )

    create table courses (
       id bigint generated by default as identity,
        course_name varchar(255),
        primary key (id)
    )

    create table emails (
       id bigint generated by default as identity,
        email_address varchar(255),
        student_id bigint,
        primary key (id)
    )

    create table student_courses (
       student_id bigint not null,
        course_id bigint not null
    )

    create table students (
       id bigint generated by default as identity,
        student_name varchar(255),
        avatar_id bigint,
        primary key (id)
    )


    alter table emails
       add constraint FKt8n2ta94msexownml4wddr6ug
       foreign key (student_id)
       references students

    alter table student_courses
       add constraint FKsfpq78oyrqua1h0obpl7ulc18
       foreign key (course_id)
       references courses

    alter table student_courses
       add constraint FKwj1l0mta35u161acdl2tupoo
       foreign key (student_id)
       references students

    alter table students
       add constraint FKaw1ddp6krgxjis7t4i5h38d4d
       foreign key (avatar_id)
       references avatars
 */