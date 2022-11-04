package pl.pacinho.codeguessrweb.model.game;

import com.google.gson.Gson;
import lombok.Getter;

@Getter
public class AnswerDto {

    private String gameId;
    private int lineNumber;
    private String file;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
