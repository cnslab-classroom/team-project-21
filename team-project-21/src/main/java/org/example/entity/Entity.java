package org.example.entity;

import org.example.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    public GamePanel gp;
    public int x,y;
    public int speed;
    public int tickCount = 0;
    public BufferedImage sprite;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Entity(GamePanel gp){
        this.gp = gp;
    }
    public void update(){
        tickCount++;
        if(tickCount==Integer.MAX_VALUE) tickCount=0;
    };
    public abstract void draw(Graphics2D g2);
}
