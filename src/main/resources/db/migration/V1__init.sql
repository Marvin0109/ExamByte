CREATE TABLE student (
    id                      UUID PRIMARY KEY,
    name                    VARCHAR(100) NOT NULL
);

CREATE TABLE professor (
    id                      UUID PRIMARY KEY,
    name                    VARCHAR(100) NOT NULL
);

CREATE TABLE korrektor (
    id                      UUID PRIMARY KEY,
    name                    VARCHAR(100) NOT NULL
);

CREATE TABLE exam (
    id                      UUID PRIMARY KEY,
    title                   VARCHAR(100) NOT NULL,
    professor_id            UUID NOT NULL,
    start_time              TIMESTAMP NOT NULL,
    end_time                TIMESTAMP NOT NULL,
    result_time             TIMESTAMP NOT NULL,
    CONSTRAINT check_exam_times CHECK(start_time < end_time AND end_time <= result_time),
    FOREIGN KEY(professor_id) REFERENCES professor(id) ON DELETE CASCADE
);

CREATE TABLE frage (
    id                      UUID PRIMARY KEY,
    frage_text              TEXT NOT NULL,
    exam_id                 UUID NOT NULL,
    max_punkte              INT NOT NULL,
    type                    TEXT NOT NULL,
    FOREIGN KEY(exam_id) REFERENCES exam(id) ON DELETE CASCADE
);

CREATE TABLE antwort (
    id                      UUID PRIMARY KEY,
    frage_id                UUID NOT NULL,
    antwort_text            VARCHAR(500),
    student_id              UUID NOT NULL,
    antwort_zeitpunkt       TIMESTAMP,
    FOREIGN KEY(frage_id) REFERENCES frage(id) ON DELETE CASCADE,
    FOREIGN KEY(student_id) REFERENCES student(id) ON DELETE CASCADE,
    CONSTRAINT unique_antwort UNIQUE (frage_id, student_id)
);

CREATE TABLE review (
    id                      UUID PRIMARY KEY,
    antwort_id              UUID NOT NULL,
    korrektor_id            UUID,
    bewertung               TEXT NOT NULL,
    punkte                  INT NOT NULL,
    FOREIGN KEY(antwort_id) REFERENCES antwort(id) ON DELETE CASCADE,
    FOREIGN KEY(korrektor_id) REFERENCES korrektor(id) ON DELETE SET NULL,
    CONSTRAINT unique_review UNIQUE (antwort_id)
);

CREATE TABLE correct_answers (
    id                      UUID PRIMARY KEY,
    frage_id                UUID NOT NULL,
    richtige_antwort        TEXT NOT NULL,
    antwort_optionen        TEXT NOT NULL,
    FOREIGN KEY(frage_id) REFERENCES frage(id) ON DELETE CASCADE,
    CONSTRAINT unique_correct_answers UNIQUE (frage_id)
);

CREATE INDEX idx_antwort_frage_student ON antwort(frage_id, student_id);
CREATE INDEX idx_review_antwort ON review(antwort_id);