package org.example.entity;

import org.example.GamePanel;

public abstract class Projectile extends Entity{
    public LivingEntity Owner;
    public int speed;
    public boolean isHit;
    public Projectile(GamePanel gp, int x, int y, LivingEntity Owner, int speed){
        super(gp,x,y);
        this.Owner = Owner;
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
