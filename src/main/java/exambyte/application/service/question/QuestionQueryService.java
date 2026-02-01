package exambyte.application.service.question;

import exambyte.application.dto.FrageDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface QuestionQueryService {

    List<FrageDTO> getFragenForExam(UUID examId);

    void createFrage(FrageDTO frageDTO);

    void createChoiceFrage(FrageDTO frageDTO, String correctAnswer, String choices);

    String getChoiceForFrage(UUID frageId);

    List<FrageDTO> getFreitextFragen(UUID examId);

    Map<UUID, FrageDTO> getFragenUUIDMap(UUID examId);
}
