package pl.pacinho.codeguessrweb.model.game;

import java.util.List;

public record RoundResultDto(String correctPath, int correctLineNumber, List<PlayerAnswerDto> playersAnswers) {

}