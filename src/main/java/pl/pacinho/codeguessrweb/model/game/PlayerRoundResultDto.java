package pl.pacinho.codeguessrweb.model.game;

import com.google.gson.Gson;

import java.math.BigDecimal;

public record PlayerRoundResultDto(BigDecimal score, String answerPath, int answerLineNumber) {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}