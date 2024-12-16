package org.example.entity;

import org.example.GamePanel;


public class AppleProjectile extends Projectile{
    public AppleProjectile(GamePanel gp, int x, int y, LivingEntity Owner, int speed){
        super(gp, x, y, Owner, speed);
        this.speed = speed;
        getImage("/textures/entities/apple.png");
        xSpeed = speed;
        ySpeed = -speed/2;
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
