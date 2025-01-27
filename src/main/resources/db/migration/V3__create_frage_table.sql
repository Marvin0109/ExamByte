create table frage(
    frage_id                serial primary key,
    frage_text              varchar(200),
    professor_id            int,
    foreign key(professor_id) references professor(id)
);
