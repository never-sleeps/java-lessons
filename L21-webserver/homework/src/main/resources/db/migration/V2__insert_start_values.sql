insert into address (id, city, street, house)
values (1, 'London', 'Trafalgar Square', '11/2/34'),
       (2, 'Moscow', 'Arbat', '1'),
       (3, 'NY', 'Valley Drive Woodside', '11377');

insert into client (id, login, password, address_id)
values (1, 'user1', 'password1', 1),
       (2, 'user2', 'password2', 2),
       (3, 'user3', 'password3', 3);

insert into phone (id, number, client_id)
values (1, '111-222', 1),
       (2, '22-33-44', 2),
       (3, '5-6-7-88', 3);