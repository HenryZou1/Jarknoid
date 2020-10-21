package Jarkanoid;

import java.awt.Graphics2D;
import java.awt.Image;

public abstract class GameObject {
	
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image image;
    
    public GameObject(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    public abstract void paintComponent(Graphics2D g2);	
		
}