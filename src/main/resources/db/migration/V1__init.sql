CREATE TABLE student (
    id                      SERIAL PRIMARY KEY,
    name                    VARCHAR(100) NOT NULL,
    fach_id                 UUID NOT NULL,
    CONSTRAINT unique_fach_id_student UNIQUE(fach_id)
);

CREATE TABLE professor (
    id                      SERIAL PRIMARY KEY,
    name                    VARCHAR(100) NOT NULL,
    fach_id                 UUID NOT NULL,
    CONSTRAINT unique_fach_id_professor UNIQUE(fach_id)
);

CREATE TABLE korrektor (
    id                      SERIAL PRIMARY KEY,
    name                    VARCHAR(100) NOT NULL,
    fach_id                 UUID NOT NULL,
    CONSTRAINT unique_fach_id_korrektor UNIQUE(fach_id)
);

CREATE TABLE exam (
    id                      SERIAL PRIMARY KEY,
    fach_id                 UUID NOT NULL,
    title                   VARCHAR(100) NOT NULL,
    professor_fach_id       UUID NOT NULL,
    start_time              TIMESTAMP NOT NULL,
    end_time                TIMESTAMP NOT NULL,
    result_time             TIMESTAMP NOT NULL,
    CONSTRAINT unique_fach_id_test UNIQUE(fach_id),
    CONSTRAINT check_exam_times CHECK(start_time < end_time AND end_time <= result_time),
    FOREIGN KEY(professor_fach_id) REFERENCES professor(fach_id) ON DELETE CASCADE
);

CREATE TABLE frage (
    id                      SERIAL PRIMARY KEY,
    frage_text              TEXT NOT NULL,
    professor_fach_id       UUID,
    exam_fach_id            UUID NOT NULL,
    fach_id                 UUID NOT NULL,
    max_punkte              INT NOT NULL,
    type                    TEXT NOT NULL,
    FOREIGN KEY(professor_fach_id) REFERENCES professor(fach_id) ON DELETE SET NULL,
    FOREIGN KEY(exam_fach_id) REFERENCES exam(fach_id) ON DELETE CASCADE,
    CONSTRAINT unique_fach_id_frage UNIQUE(fach_id)
);

CREATE TABLE antwort (
    id                      SERIAL PRIMARY KEY,
    fach_id                 UUID NOT NULL,
    frage_antwort_id        UUID NOT NULL,
    antwort_text            VARCHAR(500) NOT NULL,
    student_fach_id         UUID NOT NULL,
    FOREIGN KEY(frage_antwort_id) REFERENCES frage(fach_id) ON DELETE CASCADE,
    FOREIGN KEY(student_fach_id) REFERENCES student(fach_id) ON DELETE CASCADE,
    CONSTRAINT unique_fach_id_antwort UNIQUE(fach_id)
);

CREATE TABLE review (
    id                      SERIAL PRIMARY KEY,
    fach_id                 UUID NOT NULL,
    antwort_fach_id         UUID NOT NULL,
    korrektor_fach_id       UUID,
    bewertung               TEXT NOT NULL,
    punkte                  INT NOT NULL,
    FOREIGN KEY(antwort_fach_id) REFERENCES antwort(fach_id) ON DELETE CASCADE,
    FOREIGN KEY(korrektor_fach_id) REFERENCES korrektor(fach_id) ON DELETE SET NULL,
    CONSTRAINT unique_fach_id_review UNIQUE(fach_id)
);

CREATE TABLE correct_answers (
    id                      SERIAL PRIMARY KEY,
    fach_id                 UUID NOT NULL,
    frage_id                UUID NOT NULL,
    richtige_antwort        TEXT NOT NULL,
    antwort_optionen        TEXT NOT NULL,
    FOREIGN KEY(frage_id) REFERENCES frage(fach_id) ON DELETE CASCADE,
    CONSTRAINT unique_fach_id_correct_answers UNIQUE(fach_id)
);