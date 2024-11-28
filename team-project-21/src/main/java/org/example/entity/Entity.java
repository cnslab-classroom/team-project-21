package org.example.entity;

import org.example.GamePanel;
import org.example.utils.Mth;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    public GamePanel gp;
    public int x,prevX,y,prevY;
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
        prevX = x; prevY = y;
        tickCount++;
        if(tickCount==Integer.MAX_VALUE) tickCount=0;
    };
    public void locate(int x, int y){
        this.x=x;
        this.prevX=x;
        this.y=y;
        this.prevY=y;
    }
    public void draw(Graphics2D g2){
        BufferedImage image = sprite;
        g2.drawImage(image,Mth.lerp(prevX,x,gp.lerpProgress),Mth.lerp(prevY,y,gp.lerpProgress),gp.tileSize,gp.tileSize,null);
    }
}
