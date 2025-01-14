create table student(
    id                      serial primary key,
    name                    char(100),
    github_id               varchar(40)
);

create table professor(
    id                      serial primary key,
    name                    char(100),
    github_id               varchar(40)
);

create table frage(
    frage_id                serial primary key,
    frage_text              varchar(200)
);

create table antwort(
    antwort_id              serial primary key,
    frage_antwort_id        int,
    antwort_text            varchar(200),
    ist_korrekt             boolean,
    foreign key(frage_antwort_id) references frage(frage_id)
);