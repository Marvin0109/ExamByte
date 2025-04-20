create table student(
    id                      serial primary key,
    name                    varchar(100),
    fach_id                 uuid,
    constraint unique_fach_id_student unique(fach_id)
);

create table professor(
    id                      serial primary key,
    name                    varchar(100),
    fach_id                 uuid,
    constraint unique_fach_id_professor unique(fach_id)
);

create table korrektor(
    id                      serial primary key,
    name                    varchar(100),
    fach_id                 uuid,
    constraint unique_fach_id_korrektor unique(fach_id)
);

create table exam(
    id                      serial primary key,
    fach_id                 uuid,
    title                   varchar(100),
    professor_fach_id       uuid,
    start_time              timestamp not null,
    end_time                timestamp not null,
    result_time             timestamp not null,
    constraint unique_fach_id_test unique(fach_id),
    constraint check_exam_times check(start_time < end_time AND end_time <= result_time),
    foreign key(professor_fach_id) references professor(fach_id)
);

create type frage_type as enum ('MC', 'SC', 'FREITEXT');

create table frage(
    id                      serial primary key,
    frage_text              TEXT,
    professor_fach_id       uuid,
    exam_fach_id            uuid,
    fach_id                 uuid,
    max_punkte              int,
    type                    frage_type,
    foreign key(professor_fach_id) references professor(fach_id),
    foreign key(exam_fach_id) references exam(fach_id),
    constraint unique_fach_id_frage unique(fach_id)
);

create table antwort(
    id                      serial primary key,
    fach_id                 uuid,
    frage_antwort_id        uuid,
    antwort_text            varchar(500),
    student_fach_id         uuid,
    antwort_zeitpunkt       timestamp default current_timestamp not null,
    last_changes_zeitpunkt  timestamp default current_timestamp not null,
    foreign key(frage_antwort_id) references frage(fach_id),
    foreign key(student_fach_id) references student(fach_id),
    constraint unique_fach_id_antwort unique(fach_id)
);

create table review(
    id                      serial primary key,
    fach_id                 uuid,
    antwort_fach_id         uuid not null,
    korrektor_fach_id       uuid not null,
    bewertung               TEXT,
    punkte                  int,
    foreign key (antwort_fach_id) references antwort(fach_id),
    foreign key (korrektor_fach_id) references korrektor(fach_id),
    constraint unique_fach_id_review unique(fach_id)
);

create table correct_answers(
    id                      serial primary key,
    fach_id                 uuid,
    frage_id                uuid not null,
    richtige_antwort        TEXT,
    foreign key (frage_id) references frage(fach_id),
    constraint unique_fach_id_correct_answers unique(fach_id)
);