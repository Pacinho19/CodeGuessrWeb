package pl.pacinho.codeguessrweb.model.game;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.codeguessrweb.exception.PlayerNotFoundException;
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
    private LinkedList<Player> players;
    private LocalDateTime startTime;
    @Setter
    private String winnerInfo;
    private Code code;

    public Game(String player1) {
        players = new LinkedList<>();
        players.add(new Player(player1));
        this.id = UUID.randomUUID().toString();
        this.status = GameStatus.NEW;
        this.startTime = LocalDateTime.now();
        this.code = CodeFinderUtils.getRandomCode();
    }

    public Player getPlayer(String playerName) {
        return players.stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(playerName));
    }
}