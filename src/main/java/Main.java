import server.DiagramCreator;
import server.Server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BufferedImage img = new BufferedImage(200,100,BufferedImage.TYPE_INT_RGB);
        Stroke stroke = new BasicStroke(2f);
        Graphics2D graphics2D = (Graphics2D) img.getGraphics();

        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader("tm00.csv"));
                ){
            String line = bufferedReader.readLine();
            graphics2D.setPaint(Color.WHITE);
            graphics2D.fill(new Polygon(new int[]{0,200,200,0},new int[]{0,0,100,200},4));
            graphics2D.setColor(Color.RED);
            //graphics2D.setStroke(stroke);
            List<Double> arr = Arrays.stream(line.split(",", -1))
                    .toList()
                    .stream()
                    .map(s -> Double.parseDouble(s))
                    .toList();
            //System.out.println(arr.size());
            for (int i = 0; i < 99; i++) {
                graphics2D.draw(new Line2D.Double(i+1, (double)(img.getHeight()/2)-arr.get(i) , i+2, (double)(img.getHeight()/2)-arr.get(i + 1)));
                //System.out.println("Draw Line between: " + i +" "+ arr.get(i)+50 +" " + (i+1) + " " + arr.get(i+1)+50);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        graphics2D.drawImage(img, null, 0, 0);
        try {
            ImageIO.write(img, "JPEG", new File("amogous.jpeg"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}