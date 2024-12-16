package org.example.entity;

import org.example.GamePanel;
public class Dummy extends LivingEntity{
    public Dummy(GamePanel gp, int x, int y, String direction){
        super(gp, x, y, "enemy");
        this.direction = direction;
        getImage("/textures/entities/man_idle.png");
    }
    public int getMaxHealth(){
        return 20;
    }
}
