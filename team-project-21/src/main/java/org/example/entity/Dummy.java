package org.example.entity;

import org.example.GamePanel;
public class Dummy extends LivingEntity{
    public Dummy(GamePanel gp, int x, int y){
        super(gp, x, y, "enemy");
        state = 1;
        setImage("/textures/entities/man_idle.png");
    }
    public int getMaxHealth(){
        return 20;
    }
}
