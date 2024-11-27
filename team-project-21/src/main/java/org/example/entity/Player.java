package org.example.entity;

import org.example.GamePanel;
import org.example.KeyHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    KeyHandler keyH;
    boolean shoot = false;
    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    public void getPlayerImage(){
        try{
            sprite = ImageIO.read(getClass().getResourceAsStream("/textures/entities/mr_dummy_spawn.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setDefaultValues(){
        x = 100;
        y = 100;
        speed = 4;
    }
    @Override
    public void update(){
        super.update();
        if(shoot&& !keyH.spacePressed){
            gp.addFreshEntity(new AppleProjectile(gp, direction, x, y));
        }
        shoot = keyH.spacePressed;
        if(keyH.upPressed){
            direction = "up";
            y-=speed;
        }else if(keyH.downPressed){
            direction = "down";
            y+=speed;
        }else if(keyH.rightPressed){
            direction = "right";
            x+=speed;
        }else if(keyH.leftPressed){
            direction = "left";
            x-=speed;
        }
    }
    @Override
    public void draw(Graphics2D g2){
        BufferedImage image = sprite;
        g2.drawImage(image,x,y,gp.tileSize,gp.tileSize,null);
    }
}
