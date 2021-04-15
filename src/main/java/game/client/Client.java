package game.client;

import game.ModelSetting;
import game.enums.Move;
import game.server.Server;
import game.util.CommandGenerator;
import game.util.CommandReader;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Application {

    private int port;

    final static int WINDOW_WIDTH = 800;
    final static int WINDOW_HEIGHT = 600;

    private ModelSetting modelSetting = new ModelSetting();
    private int[][] world;
    private Rectangle[][] items;

    private static int id;
    private Socket socket;
    private Scanner reader;
    private PrintWriter writer;

    private int x;
    private int y;
    private long timer;
    private long timerEnd;

    private static MediaPlayer mediaPlayer;

    public Client(int port) {
        this.port = port;
        x = 0;
        y = 0;
        timer = System.currentTimeMillis();
        ModelSetting modelSetting = new ModelSetting();
        world = new int[20][25];
        items = new Rectangle[20][25];
        socket = new Socket();

    }

    @Override
    public void start(Stage stage) {
        System.out.println(world.length);
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", port);
            socket.connect(address, 5000);
            reader = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);
            id = Integer.parseInt(reader.nextLine());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        world = CommandReader.mapToArray(reader.nextLine());
        timerEnd = Long.parseLong(reader.nextLine());
        stage.setTitle("The Elder Scrolls V: Skyrim");
        Image image = new Image("texture/static/icon.jpg");

        /* meida
        Media media = new Media(new File("skyrim.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        */

        stage.getIcons().add(image);
        Pane group = new Pane();
        group.setId("body");
        Scene mainScene = new Scene(group, WINDOW_WIDTH + 330, WINDOW_HEIGHT);

        mainScene.getStylesheets().add("style.css");
        stage.setScene(mainScene);
        stage.setResizable(false);

        searchXAndY();
        generateScene(group);

        /*-----------------------------------------------------*/
        Rectangle background = new Rectangle();
        background.setHeight(WINDOW_HEIGHT);
        background.setWidth(320);
        background.setX(WINDOW_WIDTH + 10);
        background.getStyleClass().add("bar");
        group.getChildren().add(background);

        Text[] scoreTexts = new Text[4];
        Text[] hpTexts = new Text[4];
        Text[] protectionTexts = new Text[4];
        Text[] damageTexts = new Text[4];
        int pos = 0;
        for (int i = 0; i < 4; i++) {
            Text hp = new Text("Hp: XXX");
            Text dm = new Text("Dm: XX");
            Text pr = new Text("Pr: XX");

            hp.setX(WINDOW_WIDTH + 10 + 27);
            hp.setY(110 + pos * 40);
            hp.getStyleClass().add("smallInfo");

            dm.setX(WINDOW_WIDTH + 10 + 27 + 100);
            dm.setY(110 + pos * 40);
            dm.getStyleClass().add("smallInfo");

            pr.setX(WINDOW_WIDTH + 10 + 27 + 200);
            pr.setY(110 + pos * 40);
            pr.getStyleClass().add("smallInfo");

            Rectangle bottomBorder = new Rectangle();
            bottomBorder.setX(WINDOW_WIDTH + 40);
            bottomBorder.setY(100 + (pos + 1) * 40 + 15);
            if (i + 1 == id) {
                bottomBorder.setFill(Color.RED);
            } else
                bottomBorder.setFill(Color.WHITE);
            bottomBorder.setWidth(260);
            bottomBorder.setHeight(2);

            hpTexts[i] = hp;
            damageTexts[i] = dm;
            protectionTexts[i] = pr;

            group.getChildren().addAll(hp, dm, pr, bottomBorder);

            scoreTexts[i] = new Text("SCORE: X");
            scoreTexts[i].setX(WINDOW_WIDTH + 10 + 27);
            scoreTexts[i].setY(110 + (pos + 1) * 40);
            scoreTexts[i].getStyleClass().add("text");
            group.getChildren().add(scoreTexts[i]);
            pos += 2;
        }

        Text timeline = new Text((timerEnd - System.currentTimeMillis()) / 1000 + "");
        timeline.setX(WINDOW_WIDTH + 10 + 27);
        timeline.setY(110 + pos * 40);
        timeline.getStyleClass().add("smallInfo");

        group.getChildren().add(timeline);


        /*---FINISH---*/
        Rectangle finish = new Rectangle();
        finish.setWidth(0);
        finish.setHeight(0);
        finish.setX(0);
        finish.setY(0);
        Text finishText = new Text();
        finishText.setY(WINDOW_HEIGHT / 2 - 100);
        finishText.setX(WINDOW_HEIGHT / 3 + 200);
        finishText.visibleProperty().setValue(false);
        finishText.getStyleClass().add("finish");
        group.getChildren().addAll(finish, finishText);
        /*------------*/

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                timeline.setText("Timer: " + ((timerEnd - System.currentTimeMillis()) / 1000) + " sec");
                if ((timerEnd - System.currentTimeMillis()) <= 0) {
                    try {
                        finish.setWidth(WINDOW_WIDTH + 330);
                        finish.setHeight(WINDOW_HEIGHT);
                        finishText.visibleProperty().setValue(true);
                        writer.println("d");
                        socket.close();
                        System.out.println("OK!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.stop();
                }
            }
        };
        animationTimer.start();

        stage.show();

        AnimationTimer nextScene = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(System.currentTimeMillis() - (timerEnd + 3000) > 0){
                    try {
                        stage.close();
                        new MainWindow().start(stage);
                        this.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        nextScene.start();
        /*-----------------------------------------------------*/

        //Управление
        mainScene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                System.out.println(System.currentTimeMillis());
                if (System.currentTimeMillis() - timer < 50 || id == -1)
                    return;
                else
                    timer = System.currentTimeMillis();
                int xLast = x;
                int yLast = y;
                if (ke.getCharacter().equals("w")) {
                    move(Move.UP);
                }
                if (ke.getCharacter().equals("s")) {
                    move(Move.DOWN);
                }
                if (ke.getCharacter().equals("a")) {
                    move(Move.LEFT);
                }
                if (ke.getCharacter().equals("d")) {
                    move(Move.RIGHT);
                }
                writer.println(CommandGenerator.doTurn(id, xLast, yLast, x, y));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //Чтение с сервера
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (timerEnd > System.currentTimeMillis()) {
                    if (reader != null && reader.hasNext()) {
                        String input = reader.nextLine();
                        System.out.println("Input: " + input);
                        switch (input.charAt(0)) {
                            case 't':
                                input = input.substring(2);
                                CommandReader.turnItemByCommand(input, items);
                                CommandReader.turnIdByCommand(input, world);
                                break;
                            case 'c':
                                input = input.substring(2);
                                int hero = Integer.parseInt(input.split(",")[0]);
                                int coin = Integer.parseInt(input.split(",")[1]);
                                scoreTexts[hero - 1].setText("SCORE: " + coin);
                                break;
                            case 'u':
                                //u,id,hp,dm,pr,score
                                String datas[] = input.substring(2).split(",");
                                int heroId = Integer.parseInt(datas[0]) - 1;
                                hpTexts[heroId].setText("Hp: " + datas[1]);
                                damageTexts[heroId].setText("Dm: " + datas[2]);
                                protectionTexts[heroId].setText("Pr: " + datas[3]);
                                scoreTexts[heroId].setText("SCORE: " + datas[4]);
                                break;
                            case 'd':
                                String dDatas[] = input.substring(2).split(",");
                                int dieHeroId = Integer.parseInt(dDatas[0]);
                                int dieX = Integer.parseInt(dDatas[1]);
                                int dieY = Integer.parseInt(dDatas[2]);
                                world[dieY][dieX] = 0;
                                items[dieY][dieX].setHeight(0);
                                items[dieY][dieX].setWidth(0);
                                items[dieY][dieX] = null;
                                if (dieHeroId == id) id = -1;
                                break;
                            case 'w':
                                String rezult = "";
                                String wons[] = input.substring(2).split(",");
                                if(id == Integer.parseInt(wons[0]))
                                    rezult += "You WON!!!";
                                else
                                    rezult += "You loose!\nWon is id: " + wons[0];
                                rezult += "\nSCORE: " + wons[1];
                                finishText.setText(rezult);
                                break;
                            case 'q':
                                timerEnd = System.currentTimeMillis();
                        }
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

    }


    public void move(Move move) {
        int xLast = x;
        int yLast = y;
        if (move == Move.UP) {
            if (y > 0)
                y--;
            else
                y = 19;
        } else if (move == Move.DOWN) {
            if (y < 19)
                y++;
            else
                y = 0;
        } else if (move == Move.LEFT) {
            if (x > 0)
                x--;
            else
                x = 24;
        } else {
            if (x < 24)
                x++;
            else
                x = 0;
        }
        if (world[y][x] == -1 || (world[y][x] >= 1 && world[y][x] <= 90)) {
            if (world[y][x] != -1) {
                timer = System.currentTimeMillis() + 1000;
                writer.println(CommandGenerator.doClientAttack(id, x, y));
            }
            y = yLast;
            x = xLast;
        }
    }

    private void generateScene(Pane group) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                int id = world[i][j];
                if (id == 0) continue;
                Image image;
                Rectangle object = new Rectangle();
                object.setX(j * 32);
                object.setY(i * 30);
                object.setWidth(32);
                object.setHeight(30);
                if (id >= ModelSetting.MIN_ARMOR_ID && id < ModelSetting.MAX_ARMOR_ID) {
                    id -= ModelSetting.MIN_ARMOR_ID;
                    image = new Image(modelSetting.getArmors().get(id).getIco());
                } else if (id >= ModelSetting.MIN_WEAPON_ID && id < ModelSetting.MAX_WEAPON_ID) {
                    id -= ModelSetting.MIN_WEAPON_ID;
                    image = new Image(modelSetting.getWeapons().get(id).getIco());
                } else if (id >= ModelSetting.MIN_FOOD_ID && id < ModelSetting.MAX_FOOD_ID) {
                    id -= ModelSetting.MIN_FOOD_ID;
                    image = new Image(modelSetting.getFoods().get(id).getIco());
                } else if (id >= ModelSetting.MIN_HEROES_ID && id < ModelSetting.MAX_HEROES_ID) {
                    id -= ModelSetting.MIN_HEROES_ID;
                    image = new Image(modelSetting.getHeroes().get(id).getIco());
                } else if (id == 100) {
                    image = new Image("texture/static/coin.png");
                } else {
                    image = new Image("texture/level_design/1/wall.png");
                }
                object.setFill(new ImagePattern(image));
                group.getChildren().add(object);
                items[i][j] = object;
            }
        }
    }

    private void searchXAndY() {
        if (id == 2) {
            x = 24;
            y = 0;
        }
        if (id == 3) {
            x = 0;
            y = 19;
        }
        if (id == 4) {
            x = 24;
            y = 19;
        }
    }

}
