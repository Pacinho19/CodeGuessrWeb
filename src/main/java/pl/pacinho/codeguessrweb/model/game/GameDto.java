package pl.pacinho.codeguessrweb.model.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.pacinho.codeguessrweb.model.game.enums.GameStatus;
import pl.pacinho.codeguessrweb.model.project.dto.CodeDto;

import java.time.LocalDateTime;
import java.util.LinkedList;

@Getter
@Builder
@AllArgsConstructor
public class GameDto {

    private String id;
    private GameStatus status;
    private LinkedList<PlayerDto> players;
    private LocalDateTime startTime;
    private CodeDto code;
}
