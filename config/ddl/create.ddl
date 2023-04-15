create sequence global_seq start with 100000 increment by 1;

    create table meal (
       id integer not null,
        calories integer not null check (calories>=10 AND calories<=5000),
        date_time timestamp not null,
        description varchar(120) not null,
        user_id integer not null,
        primary key (id)
    );

    create table user_role (
       user_id integer not null,
        role varchar(255)
    );

    create table users (
       id integer not null,
        name varchar(128) not null,
        calories_per_day int default 2000 not null check (calories_per_day>=10 AND calories_per_day<=10000),
        email varchar(128) not null,
        enabled bool default true not null,
        password varchar(128) not null,
        registered timestamp default now() not null,
        primary key (id)
    );

    alter table meal 
       add constraint meals_unique_user_datetime_idx unique (user_id, date_time);

    alter table user_role 
       add constraint uk_user_role unique (user_id, role);

    alter table users 
       add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table meal 
       add constraint FK9t9sjb13rhel3nw6uqk9jd0c7 
       foreign key (user_id) 
       references users 
       on delete cascade;

    alter table user_role 
       add constraint FKj345gk1bovqvfame88rcx7yyx 
       foreign key (user_id) 
       references users 
       on delete cascade;
