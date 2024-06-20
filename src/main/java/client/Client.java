package client;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable{
    private String username;
    private String path;
    private Scanner scanner = new Scanner(System.in);
    private final Socket socket;
    private final PrintWriter writer;

    public Client(Socket socket) {
        this.socket = socket;
        OutputStream output = null;
        try {
            output = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
        writer = new PrintWriter(output, true);
    }

    public void sendData(String username,String filepath){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
            String line;
            writer.println(username);
            while((line = bufferedReader.readLine()) != null){
                System.out.println("Sent: " + line);
                TimeUnit.SECONDS.sleep(2);
                writer.println(line);
            }
            writer.println("bye");
        }catch(IOException e){
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        username = scanner.nextLine();
        path = scanner.nextLine();
        sendData(username,path);
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",6787);
        Client client = new Client(socket);
        client.run();
    }
}
