package pl.pacinho.codeguessrweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.pacinho.codeguessrweb.config.projects.ProjectsSourcesConfig;
import pl.pacinho.codeguessrweb.exception.GameNotFoundException;
import pl.pacinho.codeguessrweb.model.game.*;
import pl.pacinho.codeguessrweb.model.game.enums.GameStatus;
import pl.pacinho.codeguessrweb.model.mapper.PlayerDtoMapper;
import pl.pacinho.codeguessrweb.model.project.Code;
import pl.pacinho.codeguessrweb.repository.GameRepository;
import pl.pacinho.codeguessrweb.utils.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

import static pl.pacinho.codeguessrweb.model.game.enums.GameStatus.FINISHED;

@RequiredArgsConstructor
@Service
public class GameLogicService {

    private final GameRepository gameRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public boolean isAllPlayersFinishing(Game game) {
        return game.getPlayers()
                       .stream()
                       .allMatch(p -> p.getPlayerRoundResultDto() != null)
               & checkTheSameRound(game.getPlayers());
    }

    public boolean checkTheSameRound(LinkedList<Player> players) {
        return players.stream()
                       .map(Player::getFinishedRound)
                       .distinct()
                       .count() == 1;
    }

    public Game findById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId))
                ;
    }

    public void checkAnswer(AnswerDto answerDto, String playerName, boolean task) {
        Game game = findById(answerDto.getGameId());

        if (task)
            checkConnectionTask(game.getId(), game.getRoundNumber());

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

    public boolean checkPlayRound(GameDto gameDto, String name) {
        Game game = findById(gameDto.getId());
        return game.getPlayer(name)
                       .getFinishedRound() < game.getRoundNumber();
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

    private String getCorrectPath(Code code) {
        return StringUtils.replaceSlashes(code.filePath().replace(ProjectsSourcesConfig.PROJECTS_PATH, ""));
    }

    public GameStateDto getGameStateInfo(String gameId, String name) {
        Game game = findById(gameId);
        boolean allPlayersFinishing = isAllPlayersFinishing(game);

        if (!allPlayersFinishing) return new GameStateDto(getPlayerEndRoundMessage(name), true);

        return new GameStateDto(null, true);
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

    public void emptyAnswerOpponent(Game game) {
        checkAnswer(new AnswerDto(game.getId(), 0, ""), getBadPlayerName(game), false);
    }

    private String getBadPlayerName(Game game) {
        return game.getPlayers()
                .stream()
                .min(Comparator.comparing(Player::getFinishedRound))
                .orElseThrow(() -> new IllegalStateException("Invalid 'Bad' player function state"))
                .getName();
    }

    private void checkConnectionTask(String gameId, int roundNumber) {

        TimerTask task = new TimerTask() {
            public void run() {
                Game game = checkRound(gameId, roundNumber);
                if (game == null) return;
                checkGameState(game);
            }
        };

        if (checkRound(gameId, roundNumber) == null) return;

        new Timer("checkConnectionTaskTimer")
                .schedule(task, 1000 * 20); //20 seconds
    }

    private Game checkRound(String gameId, int roundNumber) {
        Game game = findById(gameId);
        if (game.getRoundNumber() != roundNumber) return null;
        return game;
    }


    public void checkGameState(Game game) {
        if (!checkTheSameRound(game.getPlayers())) {
            emptyAnswerOpponent(game);
            sendConnectionLostInfo(game.getId());
        }
    }

    private void sendConnectionLostInfo(String gameId) {
        simpMessagingTemplate.convertAndSend("/game-status/" + gameId, new GameStateDto("No response from opponent! Loading round result summary.", false));
    }

    public String checkEndRoundSoonMessage(String name, GameDto gameDto) {
        Game game = findById(gameDto.getId());
        if (checkTheSameRound(game.getPlayers())) return null;
        boolean canPlay = checkPlayRound(gameDto, name);
        return (canPlay ? "" : "Waiting for opponent. ")
               + "Round will be finished soon ...  ";
    }
}
