package pl.pacinho.codeguessrweb.model.game;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.codeguessrweb.model.game.enums.GameStatus;
import pl.pacinho.codeguessrweb.model.project.Code;
import pl.pacinho.codeguessrweb.utils.CodeFinderUtils;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.UUID;

@Getter
public class Game {

    private String id;
    @Setter
    private GameStatus status;
    private LinkedList<PlayerDto> players;
    private LocalDateTime startTime;
    @Setter
    private String winnerInfo;
    private Code code;

    public Game(String player1) {
        players = new LinkedList<>();
        players.add(new PlayerDto(player1));
        this.id = UUID.randomUUID().toString();
        this.status = GameStatus.NEW;
        this.startTime = LocalDateTime.now();
        this.code = CodeFinderUtils.getRandomCode();
    }

}