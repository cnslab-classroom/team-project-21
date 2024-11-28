package org.example.entity;

import org.example.GamePanel;

public class Projectile extends Entity{
    Entity Owner;
    
    public Projectile(GamePanel gp, int x, int y, Entity Owner){
        super(gp,x,y);
        this.Owner = Owner;
    }

    public Entity getOwner() {
        return Owner;
    }
    public void setOwner(Entity owner) {
        Owner = owner;
    }
}
