package server;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Handler implements Runnable{
    private String username;
    private  Server server;
    private ArrayList<String> diodes = new ArrayList<>();
    private Socket socket;
    private BufferedReader reader;

    public Handler(Socket socket,Server server){
        this.server = server;
        this.socket = socket;
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getUsername(){
        return username;
    }
    public String getDiode(int i){
        return diodes.get(i);
    }
    public void addToDB(String name, int electrode, String base64istring, DatabaseConnection databaseConnection){
        String insertSql = "INSERT INTO user_eeg (username, electrode_number, image) VALUES (?, ?, ?)";
        Connection connection = databaseConnection.getConnection();
        try{

            PreparedStatement statement = connection.prepareStatement(insertSql);
            statement.setString(1, username);
            statement.setInt(2, electrode);
            statement.setString(3, base64istring);
            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void close(){
        try {
            socket.close();
            server.removeHandler(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        try {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            String message;
            int it = 0;
            username = reader.readLine();
            databaseConnection.connect("/home/krzysztof/IdeaProjects/ClientServerPowtorka/diodes.db");
            while(!Objects.equals(message = reader.readLine(), "bye")){
                System.out.println("From: " + username+ " " + message);
                DiagramCreator diagramCreator = new DiagramCreator(new BasicStroke(1f),Color.RED);
                diagramCreator.fromCsvLine(message);
                addToDB(
                        username,
                        it++,
                        diagramCreator.Base64String("JPEG"),
                        databaseConnection);
            }
            databaseConnection.disconnect();
            close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
