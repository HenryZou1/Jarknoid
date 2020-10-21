package Jarkanoid;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class Player extends GameObject {
    
    protected int lives;
    
    public Player(int x, int y, int width, int height, int lives, Image image){
        super(x,y,width,height,image);
        this.lives = lives;
    } 
    
    public void setLives(int lives){
        this.lives = lives;
    }
    
    public boolean collision(Ball ball) {
        if (new Rectangle2D.Double(x, y, width, height).intersects(ball.x+ball.xSpeed, ball.y+ball.ySpeed, ball.width, ball.height)) {
            return true;
        } else {
            return false;
        }
    }
    
    public void paintComponent(Graphics2D g2) {
        g2.drawImage(image, x, y, null);
     
    }
}
