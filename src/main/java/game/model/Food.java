package game.model;

import lombok.ToString;

public class Food {
    private int heal;
    private String ico;


    public Food(int heal, String ico) {
        this.heal = heal;
        this.ico = ico;
    }

    public int getHeal() {
        return heal;
    }

    public String getIco() {
        return ico;
    }

    @Override
    public String toString() {
        return "Food{" +
                "heal=" + heal +
                '}';
    }
}
