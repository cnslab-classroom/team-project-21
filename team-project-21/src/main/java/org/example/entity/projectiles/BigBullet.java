package org.example.entity.projectiles;

import java.util.List;

import org.example.GamePanel;
import org.example.entity.LivingEntity;

public class BigBullet extends Projectile{
    int deathTicks;
    public BigBullet(GamePanel gp, int x, int y, LivingEntity Owner, int speed, int deathTicks){
        super(gp, x, y, Owner, speed);
        this.speed = speed;
        setImage("/textures/entities/big_bullet.png");
        xSpeed = direction=="right"? speed:-speed;
        this.deathTicks = deathTicks;
        hasGravity = false;
    }
    @Override
    public float getWidth(){
        return 0.25f;
    }
    @Override
    public float getHeight(){
        return 0.25f;
    }
    @Override
    public void update(){
        super.update();
        if(!isHit){
            List<LivingEntity> _entfound = gp.getEntitiesOfClass(LivingEntity.class, getHitbox().expand(gp.tileSize / 2));
            for(LivingEntity entityiterator : _entfound){
                if(entityiterator.isAlive()&&Owner.getTeam() != entityiterator.getTeam()){
                    entityiterator.setCurrentHealth(entityiterator.getCurrentHealth()-Owner.getAttackDamage());
                    //entityiterator.xSpeed += direction=="right"?2:-2;
                    //entityiterator.ySpeed -= 1;
                    deathTicks=tickCount;
                    isHit = true;
                    break;
                }
            }
        }
        if(tickCount>deathTicks) gp.removeP(this);
    }
}
