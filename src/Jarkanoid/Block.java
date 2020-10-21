package Jarkanoid;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;

public class Block extends GameObject {

    protected double lives;
    protected final Image[] blockIcons = {new ImageIcon("green_block.gif").getImage(), 
        new ImageIcon("purple_block.gif").getImage(), new ImageIcon("blue_block.gif").getImage(), 
        new ImageIcon("yellow_block.gif").getImage(), new ImageIcon("red_block.gif").getImage(), new ImageIcon("grey_block.gif").getImage()};
    public Block(int x, int y, int width, int height, int lives) {
        super(x, y, width, height, null);
        this.lives = lives;
        this.image = blockIcons[lives];
    }

    public void setLives(double lives) {
        this.lives = lives;
    }

    public void setImage() {
        image = blockIcons[(int)lives];
    }
    
    public String collision(Ball ball) {
        if (new Rectangle2D.Double(x, y, width, height).intersects(ball.x +ball.xSpeed, ball.y+ball.ySpeed, ball.width, ball.height)) {
            if (Math.abs(ball.y + ball.height - y) <= 4 || Math.abs(y + height - ball.y) <= 4) {
                ball.setYSpeed(-ball.ySpeed);
                return "Y";
            } else {
                ball.setXSpeed(-ball.xSpeed);
                return "X";
            }
        } else {
            return "";
        }
    }

    public void paintComponent(Graphics2D g2) {
        g2.drawImage(image, x, y, null);
    }
}