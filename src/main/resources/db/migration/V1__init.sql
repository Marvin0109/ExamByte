CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE student (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name                    VARCHAR(100) NOT NULL
);

CREATE TABLE professor (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name                    VARCHAR(100) NOT NULL
);

CREATE TABLE reviewer (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name                    VARCHAR(100) NOT NULL
);

CREATE TABLE exam (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title                   VARCHAR(100) NOT NULL,
    professor_id            UUID NOT NULL,
    start_time              TIMESTAMP NOT NULL,
    end_time                TIMESTAMP NOT NULL,
    result_time             TIMESTAMP NOT NULL,
    CONSTRAINT check_exam_times CHECK(start_time < end_time AND end_time <= result_time),
    FOREIGN KEY(professor_id) REFERENCES professor(id) ON DELETE CASCADE
);

CREATE TABLE frage (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    frage_text              TEXT NOT NULL,
    exam_id                 UUID NOT NULL,
    max_punkte              INT NOT NULL, -- Punkte * 2
    type                    TEXT NOT NULL,
    FOREIGN KEY(exam_id) REFERENCES exam(id) ON DELETE CASCADE
);

CREATE TABLE answer (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    frage_id                UUID NOT NULL,
    answer                  VARCHAR(500),
    student_id              UUID NOT NULL,
    submit_time             TIMESTAMP,
    FOREIGN KEY(frage_id) REFERENCES frage(id) ON DELETE CASCADE,
    FOREIGN KEY(student_id) REFERENCES student(id) ON DELETE CASCADE,
    CONSTRAINT unique_answer UNIQUE (frage_id, student_id)
);

CREATE TABLE review (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    answer_id               UUID NOT NULL,
    reviewer_id             UUID,
    bewertung               TEXT NOT NULL,
    punkte                  INT NOT NULL, -- Punkte * 2
    FOREIGN KEY(answer_id) REFERENCES answer(id) ON DELETE CASCADE,
    FOREIGN KEY(reviewer_id) REFERENCES reviewer(id) ON DELETE SET NULL,
    CONSTRAINT unique_review UNIQUE (answer_id)
);

CREATE TABLE correct_answers (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    frage_id                UUID NOT NULL,
    solution                TEXT NOT NULL,
    choices                 TEXT NOT NULL,
    FOREIGN KEY(frage_id) REFERENCES frage(id) ON DELETE CASCADE,
    CONSTRAINT unique_correct_answers UNIQUE (frage_id)
);

CREATE INDEX idx_answer_frage_student ON answer(frage_id, student_id);
CREATE INDEX idx_review_answer ON review(answer_id);

CREATE OR REPLACE FUNCTION set_auto_reviewer_uuid()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.name = 'Auto reviewer' THEN
        NEW.id := '11111111-1111-1111-1111-111111111111';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_set_reviewer_uuid
BEFORE INSERT ON reviewer
FOR EACH ROW
EXECUTE FUNCTION set_auto_reviewer_uuid();

CREATE OR REPLACE FUNCTION check_max_punkte_with_punkte_vergeben()
RETURNS TRIGGER AS $$
DECLARE
    max_punkte_frage INT;
BEGIN

    SELECT f.max_punkte
    INTO max_punkte_frage
    FROM frage f
    JOIN answer a ON a.frage_id = f.id
    WHERE a.id = NEW.answer_id;

    IF NEW.punkte > max_punkte_frage THEN
        RAISE EXCEPTION 'Zu viele Punkte vergeben';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER review_points_check
BEFORE INSERT OR UPDATE ON review
FOR EACH ROW
EXECUTE FUNCTION check_max_punkte_with_punkte_vergeben();