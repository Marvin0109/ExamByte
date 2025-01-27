create table antwort(
    antwort_id              serial primary key,
    frage_antwort_id        int,
    antwort_text            varchar(200),
    ist_korrekt             boolean,
    student_id              int,
    foreign key(frage_antwort_id) references frage(frage_id),
    foreign key(student_id) references student(id)
);