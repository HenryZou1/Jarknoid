package Jarkanoid;

import java.awt.Graphics2D;
import java.awt.Image;

public class Ball extends GameObject {

    protected int xSpeed;
    protected int ySpeed;
    protected boolean playerHasBall = true;

    public Ball(int x, int y, int width, int height, int xSpeed, int ySpeed, Image image) {
        super(x, y, width, height, image);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void setXSpeed(int xSpeed) {
        if (Math.abs(xSpeed) <= 6) {
            this.xSpeed = xSpeed;
        }
    }
    
    public void setYSpeed(int ySpeed) {
        if (Math.abs(ySpeed - 4) <= 2 || Math.abs(ySpeed + 4) <= 2) { 
            this.ySpeed = ySpeed;
        }
    }
    
    public void setHasBall(boolean playerHasBall){
         this.playerHasBall=playerHasBall;
 
    }

    public void paintComponent(Graphics2D g2) {
        g2.drawImage(image, x, y, null);
    }
}
