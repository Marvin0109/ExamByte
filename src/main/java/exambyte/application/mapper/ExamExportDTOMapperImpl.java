package exambyte.application.mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.domain.export_mapper.ExamExportDTOMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExamExportDTOMapperImpl implements ExamExportDTOMapper {

    @Override
    public List<ExamExportDTO> mapDTOToExport(ExamDTO exam,
                                              String profName,
                                              double punkte,
                                              List<FrageDTO> fragen,
                                              List<KorrekteAntwortenDTO> loesungen) {

        List<ExamExportDTO> export = new ArrayList<>();

        for (FrageDTO frage : fragen) {
            ExamExportDTO e = new ExamExportDTO();
            e.setExamTitle(exam.title());
            e.setAuthor(profName);
            e.setMaxPunkte(punkte);

            e.setFrageText(frage.frageText());
            e.setFrageTyp(frage.type().name());
            e.setPunkte(frage.maxPunkte());

            KorrekteAntwortenDTO k = loesungen.stream()
                    .filter(l -> l.frageId().equals(frage.id()))
                    .findAny()
                    .orElse(null);

            e.setAntwortMoeglichkeiten(k == null ? "" : k.antwortOptionen());
            e.setLoesungen(k == null ? "" : k.antworten());

            export.add(e);
        }

        return export;
    }
}
