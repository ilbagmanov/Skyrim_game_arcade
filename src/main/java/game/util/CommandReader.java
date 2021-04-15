package game.util;

import javafx.scene.shape.Rectangle;

public class CommandReader {

    public static int[][] mapToArray(String map) {
        String[] numbers = map.split(",");
        //TODO: сделать глобальным 25 и 20
        int[][] arrayMap = new int[20][25];
        int nextId = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 25; j++) {
                arrayMap[i][j] = Integer.parseInt(numbers[nextId]);
                nextId++;
            }
        }
        return arrayMap;
    }

    public static void turnItemByCommand(String s, Rectangle[][] items) {
        String[] inputs = s.split(",");
        int lastX = Integer.parseInt(inputs[1]);
        int lastY = Integer.parseInt(inputs[2]);
        int x = Integer.parseInt(inputs[3]);
        int y = Integer.parseInt(inputs[4]);
        Rectangle object = items[lastY][lastX];
        object.setX(32 * x);
        object.setY(30 * y);
        items[lastY][lastX] = null;
        if(items[y][x] != null) {
            items[y][x].setWidth(0);
            items[y][x].setHeight(0);
        }
        items[y][x] = object;
    }

    public static String turnIdByCommand(String s, int[][] world) {
        String[] inputs = s.split(",");
        int id = Integer.parseInt(inputs[0]);
        int lastX = Integer.parseInt(inputs[1]);
        int lastY = Integer.parseInt(inputs[2]);
        int x = Integer.parseInt(inputs[3]);
        int y = Integer.parseInt(inputs[4]);
        world[lastY][lastX] = 0;
        int item = world[y][x];
        world[y][x] = id;
        return (id + "," + item);
    }
}
