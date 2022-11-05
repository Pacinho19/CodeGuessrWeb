package pl.pacinho.codeguessrweb.model.game;

import com.google.gson.Gson;

import java.math.BigDecimal;

public record PlayerAnswerDto(String name, String path, int lineNumber, BigDecimal score) {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}