

import java.awt.image.BufferedImage;

public class heroplane {
    BufferedImage img;
    int x;
    int y;
    int w;
    int h;
    public heroplane(){
        img=App.getImg("/飞机0.png");
        x=200;
        y=500;
        assert img != null;
        w=(int)img.getWidth()/3;
        h=(int)img.getHeight()/3;
    }
    public void move(int mx,int my){
        x=mx-w/2;
        y=my-h/2;
    }
}
