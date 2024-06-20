package server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class DiagramCreator {
    private BufferedImage img = new BufferedImage(200,100,BufferedImage.TYPE_INT_RGB);
    private Stroke stroke;
    //new BasicStroke(1f);
    private Graphics2D graphics2D;
    //(Graphics2D) img.getGraphics();
    private Color color;

    public DiagramCreator(Stroke stroke, Color strokeColor){
        this.stroke = stroke;
        this.color = strokeColor;
        this.graphics2D = (Graphics2D) this.img.getGraphics();
    }


    public void fromCsvLine(String string){
        graphics2D.setPaint(Color.WHITE);
        graphics2D.fill(new Polygon(new int[]{0,200,200,0},new int[]{0,0,100,200},4));
        graphics2D.setColor(this.color);
        graphics2D.setStroke(this.stroke);
        List<Double> arr = Arrays.stream(string.split(",", -1))
                .toList()
                .stream()
                .map(s -> Double.parseDouble(s))
                .toList();
        for (int i = 0; i < 99; i++) {
            graphics2D.draw(new Line2D.Double(2*i, (double)(img.getHeight()/2) - arr.get(i), 2*i+2, (double)(img.getHeight()/2) - arr.get(i + 1)));
            //System.out.println("Draw Line between: " + (2*i) +" "+ (double)(img.getHeight()/2)-arr.get(i) +" " + (2*i+2) + " " + (double)(img.getHeight()/2)-arr.get(i+1));
            //200x100 i tylko 100 zajete?? plus te prostokaty? ale jak do punktu 1 w dół/górę? nie robie, robie wlasne
        }
    }

    public void generateImageToFile(String formatName, String path){
        graphics2D.drawImage(img, null, 0, 0);
        try {
            ImageIO.write(img, formatName, new File(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String Base64String(String formatName){
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try
        {
            ImageIO.write(img,formatName, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }
        catch (final IOException ioe)
        {
            throw new UncheckedIOException(ioe);
        }
    }
}
