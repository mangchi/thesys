create table if not exists users (
    id serial primary key,
    name varchar(100) not null,
    email varchar(100) unique not null,
    created_at timestamp default current_timestamp
);
select *
from users
where id = 1;