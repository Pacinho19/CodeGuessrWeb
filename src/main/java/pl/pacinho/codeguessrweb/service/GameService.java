package pl.pacinho.codeguessrweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.codeguessrweb.config.projects.ProjectsSourcesConfig;
import pl.pacinho.codeguessrweb.exception.GameNotFoundException;
import pl.pacinho.codeguessrweb.model.game.AnswerDto;
import pl.pacinho.codeguessrweb.model.game.Game;
import pl.pacinho.codeguessrweb.model.game.GameDto;
import pl.pacinho.codeguessrweb.model.game.RoundResultDto;
import pl.pacinho.codeguessrweb.model.game.enums.GameStatus;
import pl.pacinho.codeguessrweb.model.mapper.GameDtoMapper;
import pl.pacinho.codeguessrweb.repository.GameRepository;
import pl.pacinho.codeguessrweb.utils.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GameService {

    private static final int MAX_PLAYERS = 2;
    private final GameRepository gameRepository;

    public List<GameDto> getAvailableGames() {
        return gameRepository.getAvailableGames();
    }

    public String newGame(String name) {
        List<GameDto> activeGames = getAvailableGames();
        if (activeGames.size() >= 10)
            throw new IllegalStateException("Cannot create new Game! Active game count : " + activeGames.size());
        return gameRepository.newGame(name);
    }

    public GameDto findById(String gameId) {
        return GameDtoMapper.parse(gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId)))
                ;
    }

    public void joinGame(String name, String gameId) throws IllegalStateException {
        Game game = gameRepository.joinGame(name, gameId);
        if (game.getPlayers().size() == MAX_PLAYERS) game.setStatus(GameStatus.IN_PROGRESS);
    }

    public boolean checkStartGame(String gameId) {
        return findById(gameId).getPlayers().size() == MAX_PLAYERS;
    }

    public boolean canJoin(GameDto game, String name) {
        return game.getPlayers().size() < MAX_PLAYERS && game.getPlayers().stream().noneMatch(p -> p.name().equals(name));
    }


    public boolean checkPlayGame(String name, GameDto game) {
        return game.getPlayers()
                .stream()
                .anyMatch(p -> p.name().equals(name));
    }

    public void checkGamePage(GameDto game, String name) {
        if (game.getStatus() != GameStatus.IN_PROGRESS)
            throw new IllegalStateException("Game " + game.getId() + " not started !");

        if(!checkPlayGame(name, game))
            throw new IllegalStateException("Game " + game.getId() + " in progress! You can't open game page!");
    }

    public RoundResultDto checkAnswer(AnswerDto answerDto, String playerName) {
        Game game = gameRepository.findById(answerDto.getGameId()).orElseThrow(() -> new GameNotFoundException(answerDto.getGameId()));
        String correctPath = StringUtils.replaceSlashes(game.getCode().filePath().replace(ProjectsSourcesConfig.PROJECTS_PATH, ""));
        String[] partsOfCorrectPath = correctPath.split("/");
        String[] partsOfSelectedPath = answerDto.getFile().split("/");

        int correct = 0;
        for (int i = 0; i < partsOfCorrectPath.length; i++) {
            if (i > partsOfSelectedPath.length - 1) break;

            if (partsOfSelectedPath[i].equals(partsOfCorrectPath[i]))
                correct++;
            else
                break;
        }

        BigDecimal projectPoints = BigDecimal.valueOf(4_000 * ((double) correct / (double) partsOfCorrectPath.length)).setScale(0, RoundingMode.CEILING);
        double correctPercent = ((double) Math.abs(game.getCode().lineIndex() + 1 - answerDto.getLineNumber())
                / (double) game.getCode().fullCode().size()) * 100L;

        BigDecimal linePoints = !projectPoints.equals(BigDecimal.valueOf(4_000)) ? BigDecimal.ZERO : BigDecimal.valueOf(1_000L * ((100 - correctPercent) / 100));
        BigDecimal result = projectPoints.add(linePoints).setScale(0, RoundingMode.HALF_UP);

        return new RoundResultDto(result, game.getCode().lineIndex(), correctPath);
    }
}