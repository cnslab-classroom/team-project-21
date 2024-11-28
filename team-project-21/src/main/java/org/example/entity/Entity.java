package org.example.entity;

import org.example.GamePanel;
import org.example.utils.Mth;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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
    public void getImage(String input){
        try{
            sprite = ImageIO.read(getClass().getResourceAsStream(input));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void update(){
        prevX = x; prevY = y;
        tickCount++;
        if(tickCount==Integer.MAX_VALUE) tickCount=0;
    };
    //prev 좌표까지 한 번에 바꿔서 해당 엔티티가 순간이동을 한 것처럼 보여줌
    public void locate(int x, int y){
        this.x=x;
        this.prevX=x;
        this.y=y;
        this.prevY=y;
    }

    //prev 좌표는 조작하지 않기 때문에 해당 좌표로 엔티티가 재빠르게 움직이는 것처럼 보일 것임
    public void moveTo(int x, int y){
        this.x = x;
        this.y = y;
    }

    //엔티티의 겉보기 너비값 설정
    //"겉보기 넓이"라고 말하는 까닭은, 어차피 스프라이트 크기는 고정값이라고 해야하나? 어쨌든 궁금하면 사과 투사체 너비값 조정해서
    //직접 봐보는 게 빠를 것 같다.
    //결론은 그냥 캐릭터를 확대 혹은 축소하는 용도 예시를 보여주기 위해 일단 플레이어 클래스에다가 활용 사례를 보여줄게.
    public float getWidth(){
        return 1;
    }

    //엔티티의 겉보기 높이값 설정
    public float getHeight(){
        return 1;
    }

    public void draw(Graphics2D g2){
        BufferedImage image = sprite;
        g2.drawImage(image,
        Mth.lerp(prevX,x,gp.lerpProgress) - (int)(getWidth()*gp.tileSize*0.5)//스프라이트가 딱 정중앙에 오게끔 하기 위해 크기의 절반을 빼준다.
        ,Mth.lerp(prevY,y,gp.lerpProgress) - (int)(getHeight()*gp.tileSize)
        ,(int)(getWidth()*gp.tileSize),(int)(getHeight()*gp.tileSize),null);
    }
}
