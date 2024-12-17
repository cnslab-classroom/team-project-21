package org.example.entity.projectiles;

import org.example.GamePanel;
import org.example.entity.Entity;
import org.example.entity.LivingEntity;

public abstract class Projectile extends Entity{
    public LivingEntity Owner;
    public int speed, damage;
    public boolean isHit;
    public Projectile(GamePanel gp, int x, int y, LivingEntity Owner, int speed){
        super(gp,x,y);
        this.Owner = Owner;
        z= Owner.z;
        direction = Owner.direction;
        this.speed = speed;
        this.isHit = false;
    }

    public Entity getOwner() {
        return Owner;
    }
    public void setOwner(LivingEntity owner) {
        Owner = owner;
    }
}
