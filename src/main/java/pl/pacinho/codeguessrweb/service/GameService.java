package pl.pacinho.codeguessrweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.codeguessrweb.model.game.*;
import pl.pacinho.codeguessrweb.model.mapper.GameDtoMapper;
import pl.pacinho.codeguessrweb.repository.GameRepository;

import java.util.List;

import static pl.pacinho.codeguessrweb.model.game.enums.GameStatus.FINISHED;
import static pl.pacinho.codeguessrweb.model.game.enums.GameStatus.IN_PROGRESS;

@RequiredArgsConstructor
@Service
public class GameService {

    private static final int MAX_PLAYERS = 2;
    private final GameRepository gameRepository;
    private final GameLogicService gameLogicService;

    public List<GameDto> getAvailableGames() {
        return gameRepository.getAvailableGames();
    }

    public String newGame(String name) {
        List<GameDto> activeGames = getAvailableGames();
        if (activeGames.size() >= 10)
            throw new IllegalStateException("Cannot create new Game! Active game count : " + activeGames.size());
        return gameRepository.newGame(name);
    }

    public GameDto findDtoById(String gameId) {
        return GameDtoMapper.parse(gameLogicService.findById(gameId));
    }


    public void joinGame(String name, String gameId) throws IllegalStateException {
        Game game = gameRepository.joinGame(name, gameId);
        if (game.getPlayers().size() == MAX_PLAYERS) game.setStatus(IN_PROGRESS);
    }

    public boolean checkStartGame(String gameId) {
        return findDtoById(gameId).getPlayers().size() == MAX_PLAYERS;
    }

    public boolean canJoin(GameDto game, String name) {
        return game.getPlayers().size() < MAX_PLAYERS && game.getPlayers().stream().noneMatch(p -> p.equals(name));
    }

    public boolean checkPlayGame(String name, GameDto game) {
        return game.getPlayers()
                .stream()
                .anyMatch(p -> p.equals(name));
    }

    public void checkGamePage(GameDto game, String name) {
        if (game.getStatus() == FINISHED)
            throw new IllegalStateException("Game " + game.getId() + " finished!");

        if (game.getStatus() != IN_PROGRESS)
            throw new IllegalStateException("Game " + game.getId() + " not started!");

        if (!checkPlayGame(name, game))
            throw new IllegalStateException("Game " + game.getId() + " in progress! You can't open game page!");
    }

    public void checkAnswer(AnswerDto answerDto, String name) {
        gameLogicService.checkAnswer(answerDto, name, true);
    }

    public GameStateDto getGameStateInfo(String gameId, String name) {
        return gameLogicService.getGameStateInfo(gameId, name);
    }

    public RoundResultDto getRoundResultDto(String gameId) {
        return gameLogicService.getRoundResultDto(gameId);
    }

    public boolean checkPlayRound(GameDto game, String name) {
        return gameLogicService.checkPlayRound(game, name);
    }

    public String checkEndRoundSoonMessage(String name, GameDto game) {
        return gameLogicService.checkEndRoundSoonMessage(name, game);
    }

    public List<PlayerHealthInfoDto> getPlayersHealthInfo(GameDto game) {
        return gameLogicService.getPlayersHealthInfo(game);
    }
}