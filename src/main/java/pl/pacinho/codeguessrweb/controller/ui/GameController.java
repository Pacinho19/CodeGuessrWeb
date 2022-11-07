package pl.pacinho.codeguessrweb.controller.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.pacinho.codeguessrweb.config.UIConfig;
import pl.pacinho.codeguessrweb.model.game.GameDto;
import pl.pacinho.codeguessrweb.model.game.enums.GameStatus;
import pl.pacinho.codeguessrweb.service.GameService;

@RequiredArgsConstructor
@Controller
public class GameController {

    private final GameService gameService;

    @GetMapping(UIConfig.HOME)
    public String gameHome(Model model) {
        return "home";
    }

    @PostMapping(UIConfig.GAMES)
    public String availableGames(Model model) {
        model.addAttribute("games", gameService.getAvailableGames());
        return "fragments/available-games :: availableGamesFrag";
    }

    @PostMapping(UIConfig.NEW_GAME)
    public String newGame(Model model, Authentication authentication) {
        try {
            return "redirect:" + UIConfig.GAMES + "/" + gameService.newGame(authentication.getName()) + "/room";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return gameHome(model);
        }
    }

    @GetMapping(UIConfig.GAME_ROOM)
    public String gameRoom(@PathVariable(value = "gameId") String gameId, Model model, Authentication authentication) {
        try {
            GameDto game = gameService.findDtoById(gameId);
            if (game.getStatus() == GameStatus.IN_PROGRESS) return "redirect:" + UIConfig.GAMES + "/" + gameId;
            if (game.getStatus() == GameStatus.FINISHED)
                throw new IllegalStateException("Game " + gameId + " finished!");

            model.addAttribute("game", game);
            model.addAttribute("joinGame", gameService.canJoin(game, authentication.getName()));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return gameHome(model);
        }
        return "game-room";
    }

    @GetMapping(UIConfig.GAME_PAGE)
    public String gamePage(@PathVariable(value = "gameId") String gameId, Model model, Authentication authentication) {
        try {
            GameDto game = gameService.findDtoById(gameId);
            gameService.checkGamePage(game, authentication.getName());

            model.addAttribute("game", game);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return gameHome(model);
        }
        return "game";
    }

    @PostMapping(UIConfig.PLAYERS)
    public String players(@PathVariable(value = "gameId") String gameId, Model model) {
        GameDto game = gameService.findDtoById(gameId);
        model.addAttribute("game", game);
        return "fragments/game-players :: gamePlayersFrag";
    }

    @GetMapping(UIConfig.GAME_ROUND_SUMMARY)
    public String gameRoundSummary(@PathVariable(value = "gameId") String gameId, Model model) {
        try {
            model.addAttribute("roundResult", gameService.getRoundResultDto(gameId));
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return gameHome(model);
        }
        return "round-summary";
    }
}