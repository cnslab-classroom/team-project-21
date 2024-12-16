package org.example.entity.projectiles;

import org.example.GamePanel;
import org.example.entity.LivingEntity;

public class Bullet extends Projectile{
    public Bullet(GamePanel gp, int x, int y, LivingEntity Owner, int speed, int deathTicks){
        super(gp, x, y, Owner, speed);
        this.speed = speed;
        setImage("/textures/entities/bullet.png");
        xSpeed = direction=="right"? speed:-speed;
        hasGravity = false;
    }
}
