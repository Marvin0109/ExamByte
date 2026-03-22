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

CREATE TABLE question (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    text                    TEXT NOT NULL,
    exam_id                 UUID NOT NULL,
    points                  INT NOT NULL, -- Punkte * 2
    type                    TEXT NOT NULL,
    FOREIGN KEY(exam_id) REFERENCES exam(id) ON DELETE CASCADE
);

CREATE TABLE answer (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    question_id             UUID NOT NULL,
    answer                  VARCHAR(500),
    student_id              UUID NOT NULL,
    submit_time             TIMESTAMP,
    FOREIGN KEY(question_id) REFERENCES question(id) ON DELETE CASCADE,
    FOREIGN KEY(student_id) REFERENCES student(id) ON DELETE CASCADE,
    CONSTRAINT unique_answer UNIQUE (question_id, student_id)
);

CREATE TABLE review (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    answer_id               UUID NOT NULL,
    reviewer_id             UUID,
    text                    TEXT NOT NULL,
    points                  INT NOT NULL, -- Punkte * 2
    FOREIGN KEY(answer_id) REFERENCES answer(id) ON DELETE CASCADE,
    FOREIGN KEY(reviewer_id) REFERENCES reviewer(id) ON DELETE SET NULL,
    CONSTRAINT unique_review UNIQUE (answer_id)
);

CREATE TABLE correct_answers (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    question_id             UUID NOT NULL,
    solution                TEXT NOT NULL,
    choices                 TEXT NOT NULL,
    FOREIGN KEY(question_id) REFERENCES question(id) ON DELETE CASCADE,
    CONSTRAINT unique_correct_answers UNIQUE (question_id)
);

CREATE INDEX idx_answer_question_student ON answer(question_id, student_id);
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

CREATE OR REPLACE FUNCTION check_question_points_with_review_points()
RETURNS TRIGGER AS $$
DECLARE
    question_points INT;
BEGIN

    SELECT q.points
    INTO question_points
    FROM question q
    JOIN answer a ON a.question_id = q.id
    WHERE a.id = NEW.answer_id;

    IF NEW.points > question_points THEN
        RAISE EXCEPTION 'Zu viele Punkte vergeben';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER review_points_check
BEFORE INSERT OR UPDATE ON review
FOR EACH ROW
EXECUTE FUNCTION check_question_points_with_review_points();