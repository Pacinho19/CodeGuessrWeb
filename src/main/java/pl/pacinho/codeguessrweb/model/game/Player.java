package pl.pacinho.codeguessrweb.model.game;

import lombok.Getter;

@Getter
public class Player {
    private final String name;
    private HealthInfoDto healthInfoDto;
    private int finishedRound = 0;
    private PlayerRoundResultDto playerRoundResultDto;

    public void setPlayerRoundResultDto(PlayerRoundResultDto playerRoundResultDto) {
        this.playerRoundResultDto = playerRoundResultDto;
        incrementRound();
    }

    public Player(String name) {
        this.name = name;
        this.healthInfoDto = new HealthInfoDto();
    }

    private void incrementRound() {
        finishedRound++;
    }


}