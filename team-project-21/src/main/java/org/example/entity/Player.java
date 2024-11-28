package org.example.entity;

import org.example.GamePanel;
import org.example.KeyHandler;

public class Player extends Entity{
    KeyHandler keyH;
    boolean shoot = false;
    int heightDel;
    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;
        setDefaultValues();
        getImage("/textures/entities/mr_dummy_spawn.png");
        heightDel=0;
        direction = "up";
    }

    public void setDefaultValues(){
        x = 100;
        y = 100;
        speed = 12;
    }
    public float getHeight(){
        if(heightDel>0){
            return 1f + (float)(heightDel-gp.lerpProgress)/5;
        }
        return 1f;
    }
    @Override
    public void update(){
        super.update();
        if(heightDel>0)heightDel--;
        if(shoot&& !keyH.spacePressed){
            gp.addFreshEntity(new AppleProjectile(gp, direction, x, y));
            heightDel=5;
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
    /*
    @Override
    public void draw(Graphics2D g2){
        BufferedImage image = sprite;
        g2.drawImage(image,x,y,gp.tileSize,gp.tileSize,null);
    }
    */
}
