package org.example.entity;

import org.example.GamePanel;
import org.example.KeyHandler;

public class Player extends Entity{
    KeyHandler keyH;
    boolean shoot;
    int heightDel;
    public Player(GamePanel gp, KeyHandler keyH){
        super(gp, 100, 100);
        this.keyH = keyH;
        getImage("/textures/entities/mr_dummy_spawn.png");
        heightDel=0;
        direction = "right";
        shoot = false;
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
        if(shoot && !keyH.spacePressed){
            gp.addFreshEntity(new AppleProjectile(gp, direction, x, y - (int)(getHeight() * gp.tileSize)));
            heightDel=5;
        }
        shoot = keyH.spacePressed;
        /*
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
        */
    }
    /*
    @Override
    public void draw(Graphics2D g2){
        BufferedImage image = sprite;
        g2.drawImage(image,x,y,gp.tileSize,gp.tileSize,null);
    }
    */
}
