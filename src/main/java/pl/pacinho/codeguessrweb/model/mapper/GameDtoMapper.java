package pl.pacinho.codeguessrweb.model.mapper;

import pl.pacinho.codeguessrweb.model.game.Game;
import pl.pacinho.codeguessrweb.model.game.GameDto;
import pl.pacinho.codeguessrweb.model.game.PlayerDto;

public class GameDtoMapper {

    public static GameDto parse(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .startTime(game.getStartTime())
                .players(game.getPlayers().stream().map(PlayerDto::getName).toList())
                .status(game.getStatus())
                .code(CodeDtoMapper.parse(game.getCode()))
                .build();
    }
}