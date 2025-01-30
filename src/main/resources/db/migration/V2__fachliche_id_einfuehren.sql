alter table student
    add fach_id uuid default gen_random_uuid() not null;

alter table professor
    add fach_id uuid default gen_random_uuid() not null;

alter table frage
    add fach_id uuid default gen_random_uuid() not null;

alter table antwort
    add fach_id uuid default gen_random_uuid() not null;