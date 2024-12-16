package org.example.entity;

import org.example.GamePanel;

public class GunMan extends LivingEntity{
    byte state;
    public GunMan(GamePanel gp, int x, int y,String team, String direction){
        super(gp, x, y, team);
        this.state = 0;
        this.direction = direction;
        getImage("/textures/entities/gun_man/gun_man_idle.png");
    }
    public int getMaxHealth(){
        return 20;
    }
}
