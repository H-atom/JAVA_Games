

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Ep {
    BufferedImage image;
    int x;
    int y;
    int w;
    int h;
    int sp;
    public Ep(){
        Random random=new Random();
        Random random0=new Random();
        int e=random0.nextInt(6)+1;
        image=App.getImg("飞机"+e+".png");
        assert image != null;
        w=image.getWidth()/3;
        h=image.getHeight()/3;
        y=-h;
        x=random.nextInt(512-w);
        sp=15-e;

    }

    public boolean shootby(zidan zidan) {
        boolean hit=x<=zidan.x+zidan.w&&x>=zidan.x-w&&y<= zidan.y+zidan.h&&y>=zidan.y-h;
        return hit;
    }

    public boolean end(heroplane heroplane) {
        boolean hit=x<=heroplane.x+heroplane.w&&x>=heroplane.x-w&&y<=heroplane.y+heroplane.h&&y>=heroplane.y-h;
        return hit;
    }
}
