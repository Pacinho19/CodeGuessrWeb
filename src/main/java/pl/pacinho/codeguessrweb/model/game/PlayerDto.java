package pl.pacinho.codeguessrweb.model.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class PlayerDto {
    private final String name;

    @Setter
    private PlayerRoundResultDto playerRoundResultDto;
}