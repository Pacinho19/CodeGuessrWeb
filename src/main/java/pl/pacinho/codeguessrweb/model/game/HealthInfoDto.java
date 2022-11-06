package pl.pacinho.codeguessrweb.model.game;

import lombok.Getter;
import lombok.Setter;

@Getter
public class HealthInfoDto {

    private int health;
    private int prevHealth;
    @Setter
    private boolean hit;

    public HealthInfoDto() {
        this.health = 5000;
        this.prevHealth = 5000;
    }

    public void hit(int subtractHealthValue) {
        prevHealth = health;
        health = health - subtractHealthValue;
        hit = true;
    }
}