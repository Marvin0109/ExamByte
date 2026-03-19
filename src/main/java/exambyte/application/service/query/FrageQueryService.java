package exambyte.application.service.query;

import exambyte.application.dto.FrageDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FrageQueryService {

    List<FrageDTO> getFragenForExam(UUID examId);

    void createFrage(FrageDTO frageDTO);

    void createChoiceFrage(FrageDTO frageDTO, String correctAnswer, String choices);

    String getChoiceForFrage(UUID frageId);

    List<FrageDTO> getFreeResponseFragen(UUID examId);

    Map<UUID, FrageDTO> getFragenUUIDMap(UUID examId);

    FrageDTO getFrage(UUID frageId);
}
