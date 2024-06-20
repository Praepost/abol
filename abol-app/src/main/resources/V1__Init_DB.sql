create table "person" (
    "id"  serial not null,
    "password" varchar(255),
    "username" varchar(255),
    primary key ("id"));

create table "person_roles" (
    "person_id" int4 not null,
    "roles_id" int4 not null,
    primary key ("person_id", "roles_id"));

create table "roles" (
    "id"  serial not null,
    "name" varchar(20),
    primary key ("id"));

alter table "person_roles"
    add constraint "person_fk"
        foreign key ("roles_id") references "roles";
alter table "person_roles"
    add constraint "person_fk2"
        foreign key ("person_id") references "person";