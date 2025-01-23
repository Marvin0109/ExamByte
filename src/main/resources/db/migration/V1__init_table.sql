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
    frage_text              varchar(200),
    professor_id            int,
    foreign key(professor_id) references professor(id)
);

create table antwort(
    antwort_id              serial primary key,
    frage_antwort_id        int,
    antwort_text            varchar(200),
    ist_korrekt             boolean,
    student_id              int,
    foreign key(frage_antwort_id) references frage(frage_id),
    foreign key(student_id) references student(id)
);