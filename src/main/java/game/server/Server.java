package game.server;

import game.model.World;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server {
    private World world;

    public void setTimerEnd(long timerEnd) {
        this.timerEnd = timerEnd;
    }

    private long timerEnd;
    private int id;

    public Server(int port) {
        ClientHandler.autoInt = 1;
        ClientHandler.heroes = 4;
        world = new World();
        ArrayList<Thread> threads = new ArrayList<>();
        timerEnd += System.currentTimeMillis() + 120000;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            Socket client = null;
            long timer = System.currentTimeMillis();

            ServerSocket finalServer = server;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(timerEnd > System.currentTimeMillis())
                            Thread.sleep(1000);
                        InetSocketAddress address = new InetSocketAddress(finalServer.getInetAddress(), port);
                        Socket socket = new Socket();
                        socket.connect(address, 5000);
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            while (true) {
                client = server.accept();
                if(timerEnd - System.currentTimeMillis() < 0){
                    client.close();
                    break;
                }
                System.out.println("Новый клиент зарегистрирован: " + client.getInetAddress().getHostName());
                ClientHandler clientHandler = new ClientHandler(client, this);
                Thread thread = new  Thread(clientHandler);
                thread.start();
                threads.add(thread);

            }
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            try {
                threads.forEach(x -> System.out.println(x.isAlive()));
                System.out.println("STOPED");
                if (server != null)
                    server.close();
            } catch (IOException e) {
                System.out.println("ERROR");
            }
        }
    }

    public World getWorld() {
        return world;
    }

    public long getTimerEnd() {
        return timerEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
