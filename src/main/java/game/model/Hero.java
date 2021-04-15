package game.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class Hero {
    public static int MAX_HP = 100;

    private int hp;
    private Armor armor;
    private Weapon weapon;
    private int power;
    private int lucky;
    private String heroRace;
    private String ico;

    public Hero(int hp, Armor armor, Weapon weapon, int power, int lucky, String heroRace, String ico) {
        this.hp = hp;
        this.armor = armor;
        this.weapon = weapon;
        this.power = power;
        this.lucky = lucky;
        this.heroRace = heroRace;
        this.ico = ico;
    }

    public void addHp(int hp) {
        this.hp += hp;
        if (this.hp > MAX_HP) this.hp = MAX_HP;
    }


    public Hero clone() {
        return  Hero.builder()
                .heroRace(this.heroRace)
                .armor(this.armor)
                .hp(this.hp)
                .ico(this.ico)
                .lucky(this.lucky)
                .power(this.power)
                .weapon(this.weapon)
                .build();
    }
}
