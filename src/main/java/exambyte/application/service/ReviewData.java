package exambyte.application.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.service.KorrekteAntwortenService;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReviewData {

    private List<FrageDTO> fragen;
    private List<AntwortDTO> antworten;
    private List<KorrekteAntwortenDTO> korrekteAntworten;
    private final KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;
    private final KorrekteAntwortenService korrekteAntwortenService;

    public ReviewData(List<FrageDTO> fragen, List<AntwortDTO> antworten,
                      KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper,
                      KorrekteAntwortenService korrekteAntwortenService) {
        this.fragen = fragen;
        this.antworten = antworten;
        this.korrekteAntwortenDTOMapper = korrekteAntwortenDTOMapper;
        this.korrekteAntwortenService = korrekteAntwortenService;
        this.korrekteAntworten = new ArrayList<>();
    }

    public void filterToType(QuestionTypeDTO type){
        fragen = fragen.stream()
                .filter(f -> f.type().equals(type))
                .toList();

        List<UUID> frageIds = fragen.stream()
                .map(FrageDTO::fachId)
                .toList();

        antworten = antworten.stream()
                .filter(a -> frageIds.contains(a.frageFachId()))
                .toList();

        korrekteAntworten = fragen.stream()
                .map(f -> korrekteAntwortenDTOMapper.toDTO(
                        korrekteAntwortenService.findKorrekteAntwort(f.fachId())))
                .toList();
    }

    public List<FrageDTO> getFragen() {
        return fragen;
    }

    public List<AntwortDTO> getAntworten() {
        return antworten;
    }

    public List<KorrekteAntwortenDTO> getKorrekteAntworten() {
        return korrekteAntworten;
    }
}
