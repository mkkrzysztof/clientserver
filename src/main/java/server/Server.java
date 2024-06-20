package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

public class Server {
    private ArrayList<Handler> handlers = new ArrayList<>();

    public void start(int port){
        try (
                ServerSocket serverSocket = new ServerSocket(port);
        ){
            System.out.println("Server started on port " + port);
            while(true){
                Socket socket = serverSocket.accept();
                if(!socket.isClosed()){
                    Handler handler = new Handler(socket, this);
                    Thread thread = new Thread(handler);
                    thread.start();
                    handlers.add(handler);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
    public void removeHandler(Handler handler){
        handlers.remove(handler);
    }
    public void stop(){
        handlers.clear();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(6787);
    }
}
