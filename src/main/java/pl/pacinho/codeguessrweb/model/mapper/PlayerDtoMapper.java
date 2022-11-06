package pl.pacinho.codeguessrweb.model.mapper;

import pl.pacinho.codeguessrweb.model.game.Player;
import pl.pacinho.codeguessrweb.model.game.PlayerDto;

public class PlayerDtoMapper {
    public static PlayerDto parse(Player player) {
        return new PlayerDto(
                player.getName(), player.getHealthInfoDto()
        );
    }
}