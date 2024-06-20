insert into "roles" ("id", "name")
values (1, 'ROLE_USER');

insert into "roles" ("id", "name")
values (2, 'ROLE_MODERATOR');

insert into "person" ("password" , "username") values ('moderator', 'moderator');
insert into "person" ("password" , "username") values ('user', 'user');

insert into "person_roles" ("person_id", "roles_id") values (1, 1);
insert into "person_roles" ("person_id", "roles_id") values (2, 2);
