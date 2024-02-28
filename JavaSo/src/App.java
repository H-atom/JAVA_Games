

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class App {
    public static BufferedImage getImg(String path){
        try {
            BufferedImage image= ImageIO.read(App.class.getResource(path));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
