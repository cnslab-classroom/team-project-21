package org.example.entity;

import org.example.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class AppleProjectile extends Entity{
    public AppleProjectile(GamePanel gp, String direction, int x, int y){
        super(gp);
        this.direction = direction;
        getPlayerImage();
        locate(x,y);
        speed = 24;
    }
    public void getPlayerImage(){
        try{
            sprite = ImageIO.read(getClass().getResourceAsStream("/textures/entities/apple.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void update(){
        super.update();
        if(tickCount>20) gp.remove(this);
        else
            switch (direction){
            case "up" -> y-=speed;
            case "down" -> y+=speed;
            case "right" -> x+=speed;
            case "left" -> x-=speed;
            }
    }
}
