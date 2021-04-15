package game.model;

import java.util.Random;

import game.ModelSetting;
import game.enums.ModelEnum;

public class World {
    private static final Random RANDOM = new Random();
    public static final ModelSetting MODEL_SETTING = new ModelSetting();

    public static final int WALL_QUANTITY = 100;
    public static final int ARMOR_QUANTITY = 5;
    public static final int WEAPON_QUANTITY = 5;
    public static final int MONEY_QUANTITY = 30;
    public static final int FOOD_QUANTITY = 5;


    private int[][] world;
    private int[] score = new int[ModelSetting.MAX_HEROES_ID - ModelSetting.MIN_HEROES_ID + 1];
    private Hero[] heroes = new Hero[ModelSetting.MAX_HEROES_ID - ModelSetting.MIN_HEROES_ID + 1];

    public World() {
        world = new int[20][25];
        addFourHeroes(this.world);
        generateModels(WALL_QUANTITY, ModelEnum.WALL);
        generateModels(ARMOR_QUANTITY, ModelEnum.ARMOR);
        generateModels(WEAPON_QUANTITY, ModelEnum.WEAPON);
        generateModels(MONEY_QUANTITY, ModelEnum.MONEY);
        generateModels(FOOD_QUANTITY, ModelEnum.FOOD);
    }

    private void addFourHeroes(int[][] world) {
        world[0][0] = 1;
        world[0][24] = 2;
        world[19][0] = 3;
        world[19][24] = 4;
        heroes[1] = MODEL_SETTING.getHeroes().get(0).clone();
        heroes[2] = MODEL_SETTING.getHeroes().get(1).clone();
        heroes[3] = MODEL_SETTING.getHeroes().get(2).clone();
        heroes[4] = MODEL_SETTING.getHeroes().get(3).clone();

    }

    public int[][] getMap() {
        return world;
    }

    private void generateModels(int quantity, ModelEnum model) {

        for (int i = 0; i < quantity; i++) {
            int x = RANDOM.nextInt(25);
            int y = RANDOM.nextInt(20);
            if (world[y][x] == 0) {
                int id = 0;
                switch (model) {
                    case ARMOR:
                        id = ModelSetting.MIN_ARMOR_ID + RANDOM.nextInt(MODEL_SETTING.getArmors().size());
                        break;
                    case MONEY:
                        id = 100 + RANDOM.nextInt(1);
                        break;
                    case WEAPON:
                        id = ModelSetting.MIN_WEAPON_ID + RANDOM.nextInt(MODEL_SETTING.getWeapons().size());
                        break;
                    case FOOD:
                        id = ModelSetting.MIN_FOOD_ID + RANDOM.nextInt(MODEL_SETTING.getFoods().size());
                        break;
                    case WALL:
                        id = -1;
                        break;
                    case ANGRY_MOB: //ignore;
                }
                world[y][x] = id;
            } else i--;
        }
    }

    public int[] getScore() {
        return score;
    }

    public Hero[] getHeroes() {
        return heroes;
    }

    public void delete() {
        for (int i = 0; i < heroes.length; i++) {
            heroes[i].setArmor(null);
            heroes[i].setWeapon(null);
        }

    }
}
