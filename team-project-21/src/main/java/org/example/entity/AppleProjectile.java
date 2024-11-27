package org.example.entity;

import org.example.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AppleProjectile extends Entity{
    public AppleProjectile(GamePanel gp, String direction, int x, int y){
        super(gp);
        this.direction = direction;
        getPlayerImage();
        this.x = x;
        this.y = y;
        speed = 8;
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
    @Override
    public void draw(Graphics2D g2){
        BufferedImage image = sprite;
        g2.drawImage(image,x,y,gp.tileSize,gp.tileSize,null);
    }
}
