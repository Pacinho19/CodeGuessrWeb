package pl.pacinho.codeguessrweb.model.mapper;

import pl.pacinho.codeguessrweb.model.game.Game;
import pl.pacinho.codeguessrweb.model.game.GameDto;

public class GameDtoMapper {

    public static GameDto parse(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .startTime(game.getStartTime())
                .players(game.getPlayers())
                .status(game.getStatus())
                .code(CodeDtoMapper.parse(game.getCode()))
                .build();
    }
}
