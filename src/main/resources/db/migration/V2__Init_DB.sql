create sequence hibernate_sequence start 2 increment 1;

create table tovar (
        id int8 not null,
        articul varchar(255) not null,
        filename varchar(255),
        name varchar(255),
        price int4 not null,
        user_id int8,
        primary key (id)
);

create table user_role (
        user_id int8 not null,
        roles varchar(255)
);

create table usr (
        id int8 not null,
        activation_code varchar(255),
        active boolean not null,
        email varchar(255),
        password varchar(255) not null,
        username varchar(255) not null,
        primary key (id)
);

alter table if exists tovar
        add constraint tovar_user_fk
        foreign key (user_id) references usr;

alter table if exists user_role
        add constraint user_role_user_fk
        foreign key (user_id) references usr;