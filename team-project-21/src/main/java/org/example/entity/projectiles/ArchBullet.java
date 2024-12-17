package org.example.entity.projectiles;

import java.util.List;

import org.example.GamePanel;
import org.example.entity.LivingEntity;

public class ArchBullet extends Projectile{
    int deathTicks;
    public ArchBullet(GamePanel gp, int x, int y, LivingEntity Owner, int speed, int deathTicks){
        super(gp, x, y, Owner, speed);
        this.speed = speed;
        setImage("/textures/entities/big_bullet.png");
        xSpeed = direction=="right"? speed:-speed;
        ySpeed = -speed;
        this.deathTicks = deathTicks;
        hasGravity = true;
    }
    @Override
    public float getWidth(){
        return (tickCount<=deathTicks-1) ? 0.25f : 2;
    }
    @Override
    public float getHeight(){
        return (tickCount<=deathTicks-1) ? 0.25f : 2;
    }
    @Override
    public void update(){
        super.update();
        if(!isHit&&isOnGround()){
            deathTicks=tickCount+1;
            isHit = true;
            List<LivingEntity> _entfound = gp.getEntitiesOfClass(LivingEntity.class, getHitbox().expand(gp.tileSize));
            for(LivingEntity entityiterator : _entfound){
                if(entityiterator.isAlive()&&Owner.getTeam() != entityiterator.getTeam()){
                    entityiterator.setCurrentHealth(entityiterator.getCurrentHealth()-Owner.getAttackDamage());
                    //entityiterator.xSpeed += direction=="right"?2:-2;
                    //entityiterator.ySpeed -= 1;
                }
            }
        }
        if(tickCount>deathTicks-1) {
            setImage("/textures/entities/apple_explode.png");
        }
        if(tickCount>deathTicks) gp.removeP(this);
    }
}
