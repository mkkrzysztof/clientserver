package client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import server.DatabaseConnection;

import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SendDataTest{
    static DatabaseConnection databaseConnection = new DatabaseConnection();

    @BeforeAll
    static void dbconnect(){
        databaseConnection.connect("diodes.db");
    }
    @AfterAll
    static void dbdisconnect(){
        databaseConnection.disconnect();
    }

    @ParameterizedTest
    @CsvFileSource(
            files = "test2.csv",
            useHeadersInDisplayName = true)
    void sendData(
            String username,String filepath, int electrode_number, String image
    ) throws IOException, InterruptedException {

        Client client = new Client(new Socket("localhost",6787));
        client.sendData(username,filepath);
        String img = getImage(username,electrode_number);
        System.out.println(img);
        assertEquals(image,img);
        //dziala ale te test do tego nie sa dobre, bo inaczej generuje i testy sa dziwne dla 4 lini 2 wykresy????
    }

    public String getImage(String name, int electorde){
        String image= null;
        String sql = "SELECT image FROM user_eeg WHERE username = ? AND electrode_number = ?";

        try (
             PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, electorde);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    image = rs.getString("image");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return image;
    }
}
