package pl.pacinho.codeguessrweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.codeguessrweb.config.projects.ProjectsSourcesConfig;
import pl.pacinho.codeguessrweb.exception.GameNotFoundException;
import pl.pacinho.codeguessrweb.model.game.*;
import pl.pacinho.codeguessrweb.model.game.enums.GameStatus;
import pl.pacinho.codeguessrweb.model.mapper.GameDtoMapper;
import pl.pacinho.codeguessrweb.model.mapper.PlayerDtoMapper;
import pl.pacinho.codeguessrweb.model.project.Code;
import pl.pacinho.codeguessrweb.repository.GameRepository;
import pl.pacinho.codeguessrweb.utils.CodeFinderUtils;
import pl.pacinho.codeguessrweb.utils.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static pl.pacinho.codeguessrweb.model.game.enums.GameStatus.FINISHED;
import static pl.pacinho.codeguessrweb.model.game.enums.GameStatus.IN_PROGRESS;

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

    public GameDto findDtoById(String gameId) {
        return GameDtoMapper.parse(findById(gameId));
    }

    public Game findById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId))
                ;
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

    public void checkAnswer(AnswerDto answerDto, String playerName) {
        Game game = findById(answerDto.getGameId());
        Code code = game.getCurrentGameCode();
        String correctPath = getCorrectPath(code);
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
        double correctPercent = ((double) Math.abs(getCorrectLineNumber(code) - answerDto.getLineNumber())
                                 / (double) code.fullCode().size()) * 100L;

        BigDecimal linePoints = !projectPoints.equals(BigDecimal.valueOf(4_000)) ? BigDecimal.ZERO : BigDecimal.valueOf(1_000L * ((100 - correctPercent) / 100));
        BigDecimal result = projectPoints.add(linePoints).setScale(0, RoundingMode.HALF_UP);

        game.getPlayer(playerName)
                .setPlayerRoundResultDto(
                        new PlayerRoundResultDto(result, answerDto.getFile(), answerDto.getLineNumber())
                );

        damage(game);
    }

    private void damage(Game game) {
        LinkedList<Player> players = game.getPlayers();
        if (checkTheSameRound(players))
            compareScoreAndDamage(players);

        checkNextRound(game, players);
    }

    private void checkNextRound(Game game, LinkedList<Player> players) {
        if (!checkTheSameRound(players)) return;
        if (!checkPlayersHealth(players)) {
            game.setStatus(FINISHED);
            return;
        }
        nextRound(game);
    }

    private void nextRound(Game game) {
        game.nextRound();
    }

    private boolean checkPlayersHealth(LinkedList<Player> players) {
        return players.stream().allMatch(p -> p.getHealthInfoDto().getHealth() > 0);
    }

    private void compareScoreAndDamage(LinkedList<Player> players) {
        if (!checkDifferentScore(players)) {
            changeHealth(players);
            return;
        }

        Player winPlayer = players.stream()
                .max(Comparator.comparing(p -> p.getPlayerRoundResultDto().score()))
                .get();
        winPlayer.getHealthInfoDto().setHit(false);

        Player loser = getLoser(players, winPlayer);
        loser.getHealthInfoDto().hit(getDamageValue(winPlayer, loser), true);
    }

    private void changeHealth(LinkedList<Player> players) {
        players.forEach(p -> p.getHealthInfoDto().hit(0, false));
    }

    private static int getDamageValue(Player winPlayer, Player loser) {
        return winPlayer.getPlayerRoundResultDto().score().subtract(loser.getPlayerRoundResultDto().score()).intValue();
    }

    private Player getLoser(LinkedList<Player> players, Player winPlayer) {
        return players.stream()
                .filter(p -> !p.getName().equals(winPlayer.getName()))
                .findFirst()
                .get();
    }

    private boolean checkDifferentScore(LinkedList<Player> players) {
        return players.stream()
                       .filter(p -> p.getPlayerRoundResultDto() != null)
                       .map(p -> p.getPlayerRoundResultDto().score())
                       .distinct()
                       .count() > 1;
    }

    private static boolean checkTheSameRound(LinkedList<Player> players) {
        return players.stream()
                       .map(Player::getFinishedRound)
                       .distinct()
                       .count() == 1;
    }

    private String getCorrectPath(Code code) {
        return StringUtils.replaceSlashes(code.filePath().replace(ProjectsSourcesConfig.PROJECTS_PATH, ""));
    }

    public GameStateDto getGameStateInfo(String gameId, String name) {
        Game game = findById(gameId);
        boolean allPlayersFinishing = isAllPlayersFinishing(game);

        if (!allPlayersFinishing) return new GameStateDto(getPlayerEndRoundMessage(name));

        return new GameStateDto(null);
    }

    private boolean isAllPlayersFinishing(Game game) {
        return game.getPlayers()
                       .stream()
                       .allMatch(p -> p.getPlayerRoundResultDto() != null)
               & checkTheSameRound(game.getPlayers());
    }

    public RoundResultDto getRoundResultDto(String gameId) {
        Game game = findById(gameId);
        Code code = game.getEndedRoundCode();
        if (!isAllPlayersFinishing(game))
            throw new IllegalStateException("Round/Game not finished!");

        return new RoundResultDto(gameId,
                getCorrectPath(code),
                getCorrectLineNumber(code),
                getPlayerAnswers(game),
                game.getStatus() == GameStatus.IN_PROGRESS,
                getGameOverInfo(game));
    }

    private String getGameOverInfo(Game game) {
        if (game.getStatus() != FINISHED) return null;
        return "Game over ! Player " +
               getWinPlayerName(game) +
               " win the game!";
    }

    private String getWinPlayerName(Game game) {
        return game.getPlayers().stream()
                .filter(p -> p.getHealthInfoDto().getHealth() > 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Illegal players health values!"))
                .getName();
    }

    private List<PlayerAnswerDto> getPlayerAnswers(Game game) {
        return game.getPlayers()
                .stream()
                .map(this::getPlayerAnswer)
                .toList();
    }

    private PlayerAnswerDto getPlayerAnswer(Player player) {
        return new PlayerAnswerDto(PlayerDtoMapper.parse(player),
                player.getPlayerRoundResultDto().answerPath(),
                player.getPlayerRoundResultDto().answerLineNumber(),
                player.getPlayerRoundResultDto().score());
    }

    private int getCorrectLineNumber(Code code) {
        return code.lineIndex() + 1;
    }

    private String getPlayerEndRoundMessage(String name) {
        return "Player " + name + " has ended his round";
    }
}