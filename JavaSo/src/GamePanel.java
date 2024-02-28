

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    BufferedImage bg;
    heroplane heroplane=new heroplane();
    ArrayList<Ep> arrayList=new ArrayList<>();//敌机集合
    ArrayList<zidan> arrayList0=new ArrayList<>();//子弹集合

    int score;
    int es;
    int ez;
    public GamePanel(GameFrame gameFrame){
        bg=App.getImg("/背景.jpg");
        //添加鼠标相应事件
        MouseAdapter mouseAdapter=new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
             int mx=e.getX();
             int my=e.getY();
             es=mx;
             ez=my;
             heroplane.move(mx,my);
             repaint();
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        //键盘的监听事件
        KeyAdapter keyAdapter=new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keycode=e.getKeyCode();
                if (keycode==KeyEvent.VK_UP){
                    heroplane.y-=10;
                }else if (keycode==KeyEvent.VK_DOWN){
                    heroplane.y+=10;
                }else if (keycode==KeyEvent.VK_RIGHT){
                    heroplane.x+=10;
                }else if (keycode==KeyEvent.VK_LEFT){
                    heroplane.x-=10;
                }
                repaint();
            }
        };
        gameFrame.addKeyListener(keyAdapter);
    }

    //敌机的不断出现，使用线程
    public void begin(){
        new Thread(() -> {
            while (true){
                enter();//敌机出现
                epmove();//敌机移动
                shoot();//子弹发射
                fireMove();//子弹移动
                shootEp();//击落敌机
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                repaint();
            }
        }).start();
    }
    int i=0;
    protected void enter(){//敌机进场
        i++;
        if (i==8){
        Ep e=new Ep();
        arrayList.add(e);
        i=0;
        }
    }
    protected void epmove(){//敌机移动
        int x=0;
        for (int i=0;i<arrayList.size();i++){
            x++;
            Ep e=arrayList.get(i);
            e.y+=e.sp;
            if (x==20){
                e.y+=10;
                x=0;
            }
        }
    }
    int ex=0;
    int q=0;
    int c;

    protected void shoot(){//子弹发射
        ex++;
        c=20-q;
        if (ex==c){
            q++;
            if (q==19){
               q=0;
               c=2;
            }
           zidan fire=new zidan(es-60,ez-30);
           arrayList0.add(fire);
            zidan fire0=new zidan(es+10,ez-30);
            arrayList0.add(fire0);
           ex=0;
        }
    }
    protected void fireMove(){//子弹移动
        for (int i=0;i<arrayList0.size();i++){
            zidan z=arrayList0.get(i);
            z.y-=10;
        }
    }

    protected void shootEp(){
        for (int i=0;i<arrayList0.size();i++){
            zidan z=arrayList0.get(i);
            bang(z);
        }
    }

    protected void bang(zidan zidan){
        for (int i=0;i<arrayList.size();i++){
            Ep e=arrayList.get(i);
            if (e.shootby(zidan)){
                arrayList.remove(e);
                arrayList0.remove(zidan);
                score+=10;
            }
            if (e.end(heroplane)){
                arrayList.remove(e);
                int res=JOptionPane.showConfirmDialog(null,"得分为"+score,"游戏结束",JOptionPane.YES_NO_OPTION);
                if (res==JOptionPane.YES_OPTION){
                    score=0;
                }
            }
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(bg,0,0,512,768,null);
        g.drawImage(heroplane.img,heroplane.x,heroplane.y,heroplane.w,heroplane.h,null);
        for (int i=0;i<arrayList.size();i++){
            Ep ep=arrayList.get(i);
        g.drawImage(ep.image,ep.x,ep.y,ep.w,ep.h,null);
        }
        for (int i=0;i<arrayList0.size();i++){
            zidan z=arrayList0.get(i);
            g.drawImage(z.image,z.x,z.y,z.w,z.h,null);
        }
        g.setColor(Color.white);
        g.setFont(new Font("楷体",Font.BOLD,20));
        g.drawString("分数:"+score,10,30);
    }
}
