package exambyte.domain.service;

import exambyte.domain.model.aggregate.exam.Answer;

import java.util.UUID;

public interface AnswerService {

    Answer findByFrageId(UUID frageId);

    void addAnswer(Answer answer);

    Answer findByStudentAndFrage(UUID studentId, UUID frageId);

    void deleteAll();

    void deleteAnswer(UUID id);
}
