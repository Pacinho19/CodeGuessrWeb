package pl.pacinho.codeguessrweb.model.game;

import com.google.gson.Gson;

import java.util.List;

public record RoundResultDto(String correctPath, int correctLineNumber, List<PlayerAnswerDto> playersAnswers) {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}