package game.client;

import game.server.Server;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainWindow extends Application {

    final static int WINDOW_WIDTH = 800;
    final static int WINDOW_HEIGHT = 600;
    public static Server server = null;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("The Elder Scrolls V: Skyrim");
        Image image = new Image("texture/static/icon.jpg");

        stage.getIcons().add(image);
        Pane group = new Pane();
        group.setId("body");
        Scene mainScene = new Scene(group, WINDOW_WIDTH + 330, WINDOW_HEIGHT);

        mainScene.getStylesheets().add("mainWindow.css");
        stage.setScene(mainScene);
        stage.setResizable(false);

        Text title = new Text("THE ELLDER SCROLLS: SKYRIM V ONLINE");
        title.getStyleClass().add("title");
        title.setX(WINDOW_WIDTH / 6);
        title.setY(100);

        group.getChildren().add(title);

        Text writePort = new Text("PORT: ");
        writePort.setLayoutX(WINDOW_WIDTH / 3 + 35);
        writePort.setLayoutY(WINDOW_HEIGHT / 2 - 100);
        writePort.getStyleClass().add("port");

        TextField inputPort = new TextField("33333");
        inputPort.setLayoutX(WINDOW_WIDTH / 3 + 130);
        inputPort.setFocusTraversable(false);
        inputPort.setPrefColumnCount(5);
        inputPort.setLayoutY(WINDOW_HEIGHT / 2 - 152);
        inputPort.getStyleClass().add("inputPort");
        inputPort.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    inputPort.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (inputPort.getText().length() > 5) {
                    String s = inputPort.getText().substring(0, 5);
                    inputPort.setText(s);
                }
            }
        });

        Text createServer = new Text();
        createServer.setText("Create server");
        createServer.setLayoutX(WINDOW_WIDTH / 2 - 100);
        createServer.setLayoutY(WINDOW_HEIGHT / 2);
        createServer.getStyleClass().add("menuText");

        Text connectToServer = new Text();
        connectToServer.setText("Connect to server");
        connectToServer.setLayoutX(WINDOW_WIDTH / 2 - 100);
        connectToServer.setLayoutY(WINDOW_HEIGHT / 2 + 100);
        connectToServer.getStyleClass().add("menuText");

        Text quit = new Text();
        quit.setText("EXIT");
        quit.setLayoutX(WINDOW_WIDTH / 3 + 50);
        quit.setLayoutY(WINDOW_HEIGHT / 2 + 200);
        quit.getStyleClass().add("menuText");

        group.getChildren().addAll(createServer, connectToServer, quit, writePort, inputPort);
        stage.show();

        EventHandler<MouseEvent> quitClick = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.exit(0);
            }
        };
        quit.addEventFilter(MouseEvent.MOUSE_CLICKED, quitClick);

        EventHandler<MouseEvent> connectTo = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if (inputPort.getText().length() == 0)
                        return;
                    int port = Integer.parseInt(inputPort.getText());
                    Client client = new Client(port);
                    client.start(stage);
                } catch (Exception e) {
                    System.out.println(e.fillInStackTrace());
                }
            }
        };
        connectToServer.addEventFilter(MouseEvent.MOUSE_CLICKED, connectTo);

        EventHandler<MouseEvent> createTo = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if (inputPort.getText().length() == 0)
                        return;
                    int port = Integer.parseInt(inputPort.getText());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            server = new Server(port);
                        }
                    }).start();
                    Client client = new Client(port);
                    client.start(stage);
                } catch (Exception e) {
                    //System.out.println(e.fillInStackTrace());
                    System.out.println("ERROR");
                }
            }
        };
        createServer.addEventFilter(MouseEvent.MOUSE_CLICKED, createTo);


    }
}
