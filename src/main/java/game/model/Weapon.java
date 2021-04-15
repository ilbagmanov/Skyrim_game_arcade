package game.model;

import lombok.ToString;

public class Weapon {
    private int damage;
    private String ico;

    public Weapon(int damage, String ico) {
        this.damage = damage;
        this.ico = ico;
    }

    public int getDamage() {
        return damage;
    }

    public String getIco() {
        return ico;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "damage=" + damage +
                '}';
    }
}
