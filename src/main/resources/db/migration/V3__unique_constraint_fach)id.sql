alter table student
    add constraint unique_fach_id unique (fach_id);

alter table professor
    add constraint unique_fach_id unique (fach_id);

alter table frage
    add constraint unique_fach_id unique (fach_id);

alter table antwort
    add constraint unique_fach_id unique (fach_id);
