package pl.pacinho.codeguessrweb.model.game;

import com.google.gson.Gson;

import java.math.BigDecimal;

public record RoundResultDto(BigDecimal score, int correctLineNumber, String correctPath) {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}