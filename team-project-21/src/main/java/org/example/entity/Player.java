package org.example.entity;

import org.example.GamePanel;
import org.example.KeyHandler;
import org.example.entity.projectiles.AppleProjectile;

public class Player extends LivingEntity{
    KeyHandler keyH;
    boolean shoot;
    int heightDel;
    public Player(GamePanel gp, KeyHandler keyH){
        super(gp, 100, 100, "player");
        this.keyH = keyH;
        setImage("/textures/entities/shield_man/shield_man_idle.png");
        heightDel=0;
        shoot = false;
    }
    public int getMovementSpeed(){
        return 0;
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
            for(int i=0;i<10;i++)
            gp.addFreshEntityP(new AppleProjectile(gp, x, y - (int)(getHeight() * gp.tileSize), this, 20+i, 20+i));
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
