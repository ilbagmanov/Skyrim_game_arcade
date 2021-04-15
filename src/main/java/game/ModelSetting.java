package game;

import game.model.Armor;
import game.model.Food;
import game.model.Hero;
import game.model.Weapon;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class ModelSetting {
    public static final int MAX_HP = 100;

    public static final int MIN_ARMOR_ID = 1500;
    public static final int MIN_HEROES_ID = 1;
    public static final int MIN_WEAPON_ID = 800;
    public static final int MIN_FOOD_ID = 400;

    public static final int MAX_ARMOR_ID = 1700;
    public static final int MAX_HEROES_ID = 90;
    public static final int MAX_WEAPON_ID = 1000;
    public static final int MAX_FOOD_ID = 600;

    private ArrayList<Armor> armors;
    private ArrayList<Hero> heroes;
    private ArrayList<Weapon> weapons;
    private ArrayList<Food> foods;

    public ModelSetting() {
        this.armors = getAllArmors();
        this.weapons = getAllWeapons();
        this.heroes = getAllHeroes();
        this.foods = getAllFoods();
    }

    private ArrayList<Armor> getAllArmors() {
        ArrayList<Armor> armors = new ArrayList<>();

        Armor helmet = new Armor(20, "texture/static/armors/helmet.png");
        Armor boots = new Armor(20, "texture/static/armors/boot.png");
        Armor breastplate = new Armor(20, "texture/static/armors/breastplate.png");
        Armor gloves = new Armor(20, "texture/static/armors/gloves.png");
        armors.add(helmet);
        armors.add(boots);
        armors.add(breastplate);
        armors.add(gloves);

        return armors;
    }

    private ArrayList<Weapon> getAllWeapons() {
        ArrayList<Weapon> weapons = new ArrayList<>();
        Weapon sword = new Weapon(20, "texture/static/weapons/sword.png");
        Weapon mace = new Weapon(120, "texture/static/weapons/mace.png");
        Weapon bat = new Weapon(120, "texture/static/weapons/bat.png");
        Weapon axe = new Weapon(120, "texture/static/weapons/axe.png");
        weapons.add(sword);
        weapons.add(mace);
        weapons.add(bat);
        weapons.add(axe);
        return weapons;
    }

    private ArrayList<Hero> getAllHeroes() {
        ArrayList<Hero> heroes = new ArrayList<>();
        Hero men = new Hero(MAX_HP, null, null, 10, 10, "Человек", "texture/static/heroes/hero1.png");
        Hero ork = new Hero(MAX_HP, null, null, 10, 10, "Орк", "texture/static/heroes/hero2.png");
        Hero cosset = new Hero(MAX_HP, null, null, 10, 10, "Нежить", "texture/static/heroes/hero3.png");
        Hero elf = new Hero(MAX_HP, null, null, 10, 10, "Эльф", "texture/static/heroes/hero4.png");
        heroes.add(men);
        heroes.add(ork);
        heroes.add(cosset);
        heroes.add(elf);
        return heroes;
    }

    private ArrayList<Food> getAllFoods() {
        ArrayList<Food> foods = new ArrayList<>();
        Food apple = new Food(20, "texture/static/foods/apple.png");
        Food cheese = new Food(20, "texture/static/foods/cheese.png");
        Food meat = new Food(20, "texture/static/foods/meat.png");
        Food potato = new Food(20, "texture/static/foods/potato.png");
        foods.add(apple);
        foods.add(cheese);
        foods.add(meat);
        foods.add(potato);
        return foods;
    }
}
