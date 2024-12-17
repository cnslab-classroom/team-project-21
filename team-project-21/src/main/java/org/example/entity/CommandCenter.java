package org.example.entity;

import org.example.GamePanel;

public class CommandCenter extends LivingEntity{
    public CommandCenter(GamePanel gp, int x, int y, String team){
        super(gp, x, y, team);
        setImage("/textures/entities/command_center.png");
    }
    public int getMaxHealth(){
        return 200;
    }
}
