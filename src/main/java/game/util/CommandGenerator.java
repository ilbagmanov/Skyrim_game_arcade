package game.util;

import game.model.World;

public class CommandGenerator {
    public static String worldToSend(World world) {
        String answer = "";
        int[][] map = world.getMap();
        int yMax = map.length;
        int xMax = map[0].length;
        for (int i = 0; i < yMax; i++) {
            for (int j = 0; j < xMax; j++) {
                answer += map[i][j];
                if (!(i + 1 == yMax && j + 1 == xMax)) {
                    answer += ",";
                }

            }
        }
        return answer;
    }

    public static String doTurn(int id, int xLast, int yLast, int x, int y) {
        return "t," + id + "," + xLast + "," + yLast + "," + x + "," + y;
    }

    public static String takeCoin(int hero, int item) {
        return "c," + hero + "," + item;
    }

    public static String doDie(int dieHero, int x, int y, long time){return "d," + dieHero + "," + x + "," + y + "," + time;}

    public static String doClientAttack(int id, int x, int y){return "a," + id + "," + x + "," + y;}
}
