

import java.awt.image.BufferedImage;

public class zidan {
    BufferedImage image;
    int x;
    int y;
    int w;
    int h;
    public zidan(int hx,int hy){
        image=App.getImg("子弹.png");
        x=hx;
        y=hy;
        assert image != null;
        w=image.getWidth()/10;
        h=image.getHeight()/10;
    }
}
