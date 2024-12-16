package org.example.entity;

import org.example.GamePanel;

public abstract class Projectile extends Entity{
    public LivingEntity Owner;
    public int speed;
    public Projectile(GamePanel gp, int x, int y, LivingEntity Owner, int speed){
        super(gp,x,y);
        this.Owner = Owner;
        this.speed = speed;
    }

    public Entity getOwner() {
        return Owner;
    }
    public void setOwner(LivingEntity owner) {
        Owner = owner;
    }
}
