package Jarkanoid;

import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class PowerUP extends Ball {

    protected String type;
    
    public PowerUP(Image powerIcon, String type, int blockX, int blockY){ 
        super(0,0,10,10,0,0,powerIcon); 
        this.type =  type;
        guiActivate(blockX, blockY);
        
    }
    
    public void guiActivate(int newX, int newY){
        this.x = newX;
        this.y = newY;
        setYSpeed(2);
    } 
    
    
    public void guiDeactivate(){
        this.x = 0;
        this.y = 0;
        setYSpeed(0);
        setImage(null);
    }
    
    public boolean collision(Player player){
        return new Rectangle2D.Double(player.x, player.y, player.width, player.height).intersects(this.x, this.y, this.width, this.height);
    }
 
}