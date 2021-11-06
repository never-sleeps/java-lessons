insert into client (login, password)
values ('user1', 'password1'),
       ('user2', 'password2'),
       ('user3', 'password3');

insert into address (city, street, house, client_id)
values ('London', 'Trafalgar Square', '11/2/34', 1),
       ('Moscow', 'Arbat', '1', 2),
       ('NY', 'Valley Drive Woodside', '11377', 3);

insert into phone (number, client_id)
values ('111-222', 1),
       ('22-33-44', 2),
       ('5-6-7-88', 3);