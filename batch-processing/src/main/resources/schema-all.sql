create table if not exists task (
    task_id varchar(255) not null ,
    skill varchar(255) not null ,
    primary key (task_id, skill)
);

create table if not exists team (
    team_id varchar(255) primary key
);

create table if not exists team_skill (
    team_id varchar(255),
    skill varchar(255),
    primary key (team_id, skill)
)