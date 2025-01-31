create table student(
                        id                      serial primary key,
                        name                    varchar(100),
                        fach_id                 uuid default gen_random_uuid() not null,
                        constraint unique_fach_id_student unique(fach_id)
);

create table professor(
                          id                      serial primary key,
                          name                    varchar(100),
                          fach_id                 uuid default gen_random_uuid() not null,
                          constraint unique_fach_id_professor unique(fach_id)
);

create table frage(
                      id                      serial primary key,
                      frage_text              varchar(200),
                      professor_fach_id       uuid,
                      fach_id                 uuid default gen_random_uuid() not null,
                      foreign key(professor_fach_id) references professor(fach_id),
                      constraint unique_fach_id_frage unique(fach_id)
);

create table antwort(
                        id                      serial primary key,
                        frage_antwort_id        uuid,
                        antwort_text            varchar(500),
                        ist_korrekt             boolean,
                        student_fach_id         uuid,
                        fach_id                 uuid default gen_random_uuid() not null,
                        foreign key(frage_antwort_id) references frage(fach_id),
                        foreign key(student_fach_id) references student(fach_id),
                        constraint unique_fach_id_antwort unique(fach_id)
);