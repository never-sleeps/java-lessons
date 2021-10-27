drop table if exists address cascade;
drop table if exists client cascade;
drop table if exists phone cascade;
drop sequence if exists address_sequence;
drop sequence if exists client_sequence;
drop sequence if exists phone_sequence;

create sequence address_sequence start 4 increment 1 no minvalue no maxvalue cache 1;
create sequence client_sequence start 4 increment 1 no minvalue no maxvalue cache 1;
create sequence phone_sequence start 4 increment 1 no minvalue no maxvalue cache 1;

create table address
(
    id     bigint       not null primary key,
    city   varchar(255) not null,
    house  varchar(255),
    street varchar(255)
);

create table client
(
    id         bigint       not null primary key,
    login      varchar(255) not null,
    password   varchar(255) not null,
    address_id int8
);

create table phone
(
    id        bigint       not null primary key,
    number    varchar(255) not null,
    client_id int8
);
