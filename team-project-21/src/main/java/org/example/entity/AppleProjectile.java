package org.example.entity;

import org.example.GamePanel;


public class AppleProjectile extends Entity{
    public AppleProjectile(GamePanel gp, String direction, int x, int y){
        super(gp);
        this.direction = direction;
        locate(x,y);
        speed = 24;
        getImage("/textures/entities/apple.png");
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
        else
            switch (direction){
            case "up" -> y-=speed;
            case "down" -> y+=speed;
            case "right" -> x+=speed;
            case "left" -> x-=speed;
            }
    }
}
