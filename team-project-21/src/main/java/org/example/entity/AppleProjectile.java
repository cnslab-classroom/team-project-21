package org.example.entity;

import java.util.List;

import org.example.GamePanel;


public class AppleProjectile extends Projectile{
    int deathTicks;
    public AppleProjectile(GamePanel gp, int x, int y, LivingEntity Owner, int speed, int deathTicks){
        super(gp, x, y, Owner, speed);
        this.speed = speed;
        getImage("/textures/entities/apple.png");
        xSpeed = direction=="right"? speed:-speed;
        ySpeed = -speed/2;
        this.deathTicks = deathTicks;
    }
    @Override
    public float getWidth(){
        return (tickCount<=deathTicks-3) ? 0.5f : 1;
    }
    @Override
    public float getHeight(){
        return (tickCount<=deathTicks-3) ? 0.5f : 1;
    }
    @Override
    public void update(){
        super.update();
        if(!isHit){
            List<LivingEntity> _entfound = gp.getEntitiesOfClass(LivingEntity.class, getHitbox());
            for(LivingEntity entityiterator : _entfound){
                if(entityiterator.isAlive()&&Owner.getTeam() != entityiterator.getTeam()){
                    entityiterator.setCurrentHealth(entityiterator.getCurrentHealth()-5);
                    entityiterator.xSpeed += direction=="right"?10:-10;
                    entityiterator.ySpeed -= 5;
                    deathTicks = tickCount+2;
                    break;
                }
            }
        }
        if(tickCount>deathTicks-3) {
            isHit = true;
            getImage("/textures/entities/apple_explode.png");
        }
        if(tickCount>deathTicks) gp.remove(this);
    }
}
