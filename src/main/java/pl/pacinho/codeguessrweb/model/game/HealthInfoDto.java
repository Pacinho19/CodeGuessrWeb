package pl.pacinho.codeguessrweb.model.game;

import lombok.Getter;

@Getter
public class HealthInfoDto {

    private int health;
    private int prevHealth;

    public HealthInfoDto() {
        this.health = 5000;
        this.prevHealth = 5000;
    }

    public void changeHealth(int subtractHealthValue) {
        prevHealth = health;
        health = health - subtractHealthValue;
    }
}