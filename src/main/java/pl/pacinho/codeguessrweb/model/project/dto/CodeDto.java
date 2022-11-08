package pl.pacinho.codeguessrweb.model.project.dto;

import java.util.List;

public record CodeDto(String roundId, List<LineDto> lines) {

}
