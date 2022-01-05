drop table if exists address cascade;
drop table if exists client cascade;
drop table if exists phone cascade;

create table client
(
    id       bigserial    not null primary key,
    login    varchar(255) not null,
    password varchar(255) not null
);

create table address
(
    id        bigserial    not null primary key,
    city      varchar(255) not null,
    client_id bigint
);

create table phone
(
    id        bigserial    not null primary key,
    number    varchar(255) not null,
    client_id bigint
);

alter table address
    add foreign key (client_id) references client (id);
alter table phone
    add foreign key (client_id) references client (id);