package org.example.entity;

import org.example.GamePanel;
public class Dummy extends LivingEntity{
    public Dummy(GamePanel gp, int x, int y, int z){
        super(gp, x, y, z, "enemy");
        state = 1;
        setImage("/textures/entities/man_idle.png");
    }
    public float getWidth(){
        return 0.9f;
    }
    public float getHeight(){
        return 0.9f;
    }
    public int getMaxHealth(){
        return 20;
    }
}
