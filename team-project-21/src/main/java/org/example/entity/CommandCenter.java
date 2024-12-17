package org.example.entity;

import java.awt.image.BufferedImage;

import org.example.GamePanel;

public class CommandCenter extends LivingEntity{
    public BufferedImage[] stateSpirtes = new BufferedImage[4];

    public CommandCenter(GamePanel gp, int x, int y, String team){
        super(gp, x, y, team);
        z = -75;
        stateSpirtes[0] = getImage("/textures/entities/command_center.png");
        stateSpirtes[1] = getImage("/textures/entities/command_center_damaged1.png");
        stateSpirtes[2] = getImage("/textures/entities/command_center_damaged2.png");
        stateSpirtes[3] = getImage("/textures/entities/command_center_damaged3.png");
    }
    public void update(){
        super.update();
        if(getCurrentHealth() > getMaxHealth() * 0.75)
            state = 0;
        else if(getCurrentHealth() > getMaxHealth() * 0.5)
            state = 1;
        else if(getCurrentHealth() > getMaxHealth() * 0.25)
            state = 2;
        else
            state = 3;
        sprite = stateSpirtes[state];
    }
    public int getZ(){
        return -300;
    }
    public void travel(){
    }
    public int getMaxHealth(){
        return 1000;
    }
    public float getWidth(){
        return 6;
    }
    public float getHeight(){
        return 6;
    }
}
