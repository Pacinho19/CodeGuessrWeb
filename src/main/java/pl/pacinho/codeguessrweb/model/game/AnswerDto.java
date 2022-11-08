package pl.pacinho.codeguessrweb.model.game;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AnswerDto {

    private String gameId;
    private String roundId;
    private int lineNumber;
    private String file;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
