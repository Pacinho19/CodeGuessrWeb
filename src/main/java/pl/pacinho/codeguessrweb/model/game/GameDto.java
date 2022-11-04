package pl.pacinho.codeguessrweb.model.game;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.codeguessrweb.model.game.enums.GameStatus;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class GameDto {

    private String id;
    @Setter
    private GameStatus status;
    private LinkedList<PlayerDto> players;
    private LocalDateTime startTime;
    @Setter
    private String winnerInfo;
    @Setter
    private List<String> wordLetters;
    @Setter
    private List<String> definition;
    @Setter
    private List<String> images;

    public GameDto(String player1) {
        players = new LinkedList<>();
        players.add(new PlayerDto(player1));
        this.id = UUID.randomUUID().toString();
        this.status = GameStatus.NEW;
        this.startTime = LocalDateTime.now();
    }

}