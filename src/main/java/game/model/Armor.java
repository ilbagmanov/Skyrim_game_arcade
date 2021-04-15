package game.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
public class Armor {
    private int protection;
    private String ico;

    public Armor(int protection, String ico) {
        this.protection = protection;
        this.ico = ico;
    }

    @Override
    public String toString() {
        return "Armor{" +
                "protection=" + protection +
                '}';
    }
}
