package org.example.entity;

import org.example.GamePanel;


public class AppleProjectile extends Entity{
    public int speed;
    public AppleProjectile(GamePanel gp, String direction, int x, int y){
        super(gp, x, y);
        this.direction = direction;
        locate(x,y);
        speed = 24;
        getImage("/textures/entities/apple.png");
        switch (direction){
            case "up":
                ySpeed = -speed;
                break;
            case "down":
                ySpeed = speed;
                break;
            case "right":
                xSpeed = speed;
                ySpeed = -speed/2;
                break;
            case "left":
                xSpeed = -speed;
                ySpeed = -speed/2;
                break;
        }
    }
    @Override
    public float getWidth(){
        return 0.5f;
    }
    @Override
    public float getHeight(){
        return 0.5f;
    }
    @Override
    public void update(){
        super.update();
        if(tickCount>20) gp.remove(this);
        
    }
}
