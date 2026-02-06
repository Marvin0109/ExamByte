package exambyte.web.service;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.VersuchDTO;

import java.util.List;
import java.util.UUID;

public interface HelperService {

    List<VersuchDTO> getValidAttempts(String studentName);

    String getExamAvailabilityNotice(ExamDTO dto);

    String getTimeDifference(ExamDTO dto);

    List<String> split(String toSplit);

    List<String> splitOldDataMC(String toSplit);

    PreparedFrageData prepareFrageData(FrageDTO frage, UUID studentId);
}
