

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(){
        setTitle("天天打飞机");
        setSize(512,768);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    public static void main(String[] args) {
        GameFrame gameFrame=new GameFrame();
        GamePanel gamePanel=new GamePanel(gameFrame);
        gamePanel.begin();
        gameFrame.add(gamePanel);
        gameFrame.setVisible(true);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
