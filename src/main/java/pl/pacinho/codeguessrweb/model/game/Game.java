package pl.pacinho.codeguessrweb.model.game;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.codeguessrweb.exception.PlayerNotFoundException;
import pl.pacinho.codeguessrweb.model.game.enums.GameStatus;
import pl.pacinho.codeguessrweb.model.project.Code;
import pl.pacinho.codeguessrweb.utils.CodeFinderUtils;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class Game {

    private String id;
    @Setter
    private GameStatus status;
    private LinkedList<Player> players;
    private LocalDateTime startTime;
    @Setter
    private String winnerInfo;
    private List<Code> codes;
    private int roundNumber;

    public Game(String player1) {
        players = new LinkedList<>();
        players.add(new Player(player1));
        this.id = UUID.randomUUID().toString();
        this.status = GameStatus.NEW;
        this.startTime = LocalDateTime.now();
        this.codes = new ArrayList<>();
        codes.add(CodeFinderUtils.getRandomCode());
        this.roundNumber = 1;
    }

    public Player getPlayer(String playerName) {
        return players.stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(playerName));
    }

    public void nextRound() {
        codes.add(CodeFinderUtils.getRandomCode());
        roundNumber++;
    }

    public Code getEndedRoundCode() {
        return status == GameStatus.FINISHED ? getCurrentGameCode() : codes.get(roundNumber - 2);
    }

    public Code getCurrentGameCode() {
        return codes.get(roundNumber - 1);
    }
}