package game.server;

import game.ModelSetting;
import game.model.Hero;
import game.model.World;
import game.util.CommandGenerator;
import game.util.CommandReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable {

    private static HashMap<Integer, PrintWriter> map = new HashMap<>();
    public static int autoInt = 1;

    private Server server;
    private Socket client;
    private BufferedReader reader;
    private PrintWriter writer;
    private int id;
    private int takedItems = 0;
    private int takedCoinds = 0;
    public static int heroes = 4;

    public ClientHandler(Socket client, Server server) {
        this.server = server;
        id = -1;
        for(int i = autoInt; i < 5; i++) {
            if(server.getWorld().getHeroes()[i].getHp() > 0){
                id = i;
                break;
            }
            autoInt++;
        }
        autoInt++;
        try {
            this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.writer = new PrintWriter(client.getOutputStream(), true);
            map.put(id, writer);
        } catch (IOException e) {
            throw new IllegalStateException("ClientHandler IOException");
        }
        this.client = client;
        writer.println(id + "\n" + CommandGenerator.worldToSend(server.getWorld()) + "\n" + server.getTimerEnd());
        writer.flush();
        updateInfo();
    }

    @Override
    public void run() {
        try {
            while (!client.isInputShutdown()) {
                String input = reader.readLine();
                System.out.println(input);
                System.out.println("Получил: " + input);
                if (input.charAt(0) == 't') {
                    input = input.substring(2);
                    String itemString[] = CommandReader.turnIdByCommand(input, server.getWorld().getMap()).split(",");
                    int hero = Integer.parseInt(itemString[0]);
                    int item = Integer.parseInt(itemString[1]);
                    msgAll("t," + input);
                    takeItem(hero, item);
                } else if (input.charAt(0) == 'a') {
                    String datas[] = input.substring(2).split(",");
                    int id = Integer.parseInt(datas[0]);
                    int x = Integer.parseInt(datas[1]);
                    int y = Integer.parseInt(datas[2]);
                    int dieHero = server.getWorld().getMap()[y][x];
                    if (dieHero >= ModelSetting.MIN_HEROES_ID && dieHero <= ModelSetting.MAX_HEROES_ID) {
                        Hero starterAttact = server.getWorld().getHeroes()[id];
                        Hero attackedHero = server.getWorld().getHeroes()[dieHero];
                        int damage = -1 * (starterAttact.getPower());
                        if (starterAttact.getWeapon() != null)
                            damage -= starterAttact.getWeapon().getDamage();
                        if (attackedHero.getArmor() != null)
                            damage += attackedHero.getArmor().getProtection();
                        if (damage < 0)
                            attackedHero.addHp(damage);
                        if (attackedHero.getHp() <= 0) {
                            attackedHero.setHp(0);
                            server.getWorld().getMap()[y][x] = 0;
                            msgAll(CommandGenerator.doDie(dieHero, x, y, 1000));
                            server.getWorld().getScore()[id] += 250;
                            heroes--;
                        }
                        server.getWorld().getScore()[id] += 50;
                    }
                } else if (input.charAt(0) == 'd') {
                    server.getWorld().delete();
                    System.out.println("SUCCESS DELETE");
                    break;
                }
                updateInfo();
                if (heroes == 1) {
                    msgAll("q");
                    server.setTimerEnd(System.currentTimeMillis() + 1000);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            server.getWorld().delete();
            map.clear();
            System.out.println("World was deleted!");
            try {
                client.close();
            } catch (IOException e) {
                //ignore
            }
        } catch (Exception e) {
            //ingore
        }
    }

    public void msgAll(String s) {
        map.forEach((x, writer) -> {
            writer.println(s);
        });
    }

    private void takeItem(int hero, int item) {
        if (item >= ModelSetting.MIN_ARMOR_ID && item < ModelSetting.MAX_ARMOR_ID) {
            item -= ModelSetting.MIN_ARMOR_ID;
            server.getWorld().getHeroes()[hero].setArmor(World.MODEL_SETTING.getArmors().get(item));
            takedItems++;
        } else if (item >= ModelSetting.MIN_WEAPON_ID && item < ModelSetting.MAX_WEAPON_ID) {
            item -= ModelSetting.MIN_WEAPON_ID;
            server.getWorld().getHeroes()[hero].setWeapon(World.MODEL_SETTING.getWeapons().get(item));
            takedItems++;
        } else if (item >= ModelSetting.MIN_FOOD_ID && item < ModelSetting.MAX_FOOD_ID) {
            item -= ModelSetting.MIN_FOOD_ID;
            server.getWorld().getHeroes()[hero].addHp((World.MODEL_SETTING.getFoods().get(item)).getHeal());
            takedItems++;
        } else if (item == 100) {
            server.getWorld().getScore()[hero] += 100;
            msgAll("c," + hero + "," + server.getWorld().getScore()[hero]);
            takedCoinds++;
        }
        msgAll(server.getWorld().getHeroes()[hero].toString());
        if (takedCoinds == World.MONEY_QUANTITY) {
            msgAll("Yheaaa!!!");
            takedCoinds++;
        }
        if (takedItems == (World.ARMOR_QUANTITY + World.FOOD_QUANTITY + World.WEAPON_QUANTITY)) {
            msgAll("Woow!!!");
            takedItems++;
        }
    }

    private void updateInfo() {
        //u,id,hp,dm,pr,score
        for (int i = 1; i <= 4; i++) {
            Hero hero = server.getWorld().getHeroes()[i];
            int score = server.getWorld().getScore()[i];
            String answer = "u," + i + ","
                    + hero.getHp() + ",";
            if (hero.getWeapon() != null)
                answer += hero.getWeapon().getDamage() + ",";
            else
                answer += 0 + ",";
            if (hero.getArmor() != null)
                answer += +hero.getArmor().getProtection() + ",";
            else
                answer += +0 + ",";
            answer += score;
            msgAll(answer);
        }
        int hero = -1;
        int score = -1;
        for (int i = 0; i < server.getWorld().getScore().length; i++) {
            if (score < server.getWorld().getScore()[i]) {
                hero = i;
                score = server.getWorld().getScore()[i];
            }
        }
        msgAll("w," + hero + "," + score);
    }
}
