package pl.pacinho.codeguessrweb.repository;

import org.springframework.stereotype.Repository;
import pl.pacinho.codeguessrweb.exception.GameNotFoundException;
import pl.pacinho.codeguessrweb.model.game.GameDto;
import pl.pacinho.codeguessrweb.model.game.PlayerDto;
import pl.pacinho.codeguessrweb.model.game.enums.GameStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GameRepository {
    private Map<String, GameDto> gameMap;

    public GameRepository() {
        gameMap = new HashMap<>();
    }

    public String newGame(String playerName) {
        GameDto game = new GameDto(playerName);
        gameMap.put(game.getId(), game);
        return game.getId();
    }

    public List<GameDto> getAvailableGames() {
        return gameMap.values()
                .stream()
                .filter(game -> game.getStatus() != GameStatus.FINISHED)
                .toList();
    }

    public Optional<GameDto> findById(String gameId) {
        return Optional.ofNullable(gameMap.get(gameId));
    }

    public GameDto joinGame(String name, String gameId) throws IllegalStateException {
        GameDto game = gameMap.get(gameId);
        if (game == null)
            throw new GameNotFoundException(gameId);

        if (game.getStatus() != GameStatus.NEW)
            throw new IllegalStateException("Cannot join to " + gameId + ". Game status : " + game.getStatus());

        if (game.getPlayers().get(0).name().equals(name))
            throw new IllegalStateException("Game " + gameId + " was created by you!");

        game.getPlayers().add(new PlayerDto(name));
        return game;
    }

}