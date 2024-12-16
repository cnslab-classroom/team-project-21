package org.example.entity;

import org.example.GamePanel;
import org.example.utils.Mth;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Entity {
    public GamePanel gp;
    public int x,prevX,y,prevY,screenX,screenY;
    public int xSpeed, ySpeed;
    public int tickCount = 0;
    public BufferedImage sprite;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public boolean noCulling, hasGravity;
    private HitBox hitbox;


    public Entity(GamePanel gp, int x, int y){
        this.gp = gp;
        noCulling = false;
        hasGravity = true;
        xSpeed = ySpeed = 0;
        locate(x, y);
        this.hitbox = createHitbox();
    }
    public HitBox createHitbox() {
        return new HitBox(x, y, 0, getWidth(), getHeight(), getWidth());
    }
    public HitBox getHitbox() {
        return hitbox;
    }

    public void updateHitbox() {
        // 히트박스를 현재 위치로 동기화
        hitbox.setX(x);
        hitbox.setY(y);
    }

    public void setHitbox(HitBox hitbox) {
        this.hitbox = hitbox;
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
        if(hasGravity && !isOnGround()){
            ySpeed += getGravitySpeed();
        }
        push(xSpeed, ySpeed);
        if(tickCount==Integer.MAX_VALUE) tickCount=0;

        updateHitbox();
    };

    int getGravitySpeed(){
        return 3;
    }

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
    public void push(int vectorX, int vectorY){
        this.x += vectorX;
        this.y += vectorY;
        if(isOnGround()){
            y = 500;
            ySpeed = 0;
            xSpeed *= 0.5;
        }
    }
    public boolean isOnGround(){
        return y>500;
    }
    
    
    
    public float getWidth(){
        return 1;
    }

    //엔티티의 겉보기 높이값 설정
    public float getHeight(){
        return 1;
    }

    public void draw(Graphics2D g2){
        boolean canRender= true;
        if(!noCulling &&
        (gp.actualX + x + getWidth() * gp.tileSize/2 < 0 ||
        gp.actualX + x - getWidth() * gp.tileSize > gp.screenWidth ||
        gp.actualY + y + getHeight() * gp.tileSize < 0 ||  // 위쪽 화면 바깥
        gp.actualY + y - getHeight() * gp.tileSize > gp.screenHeight)){
            canRender = false;
        }
        if(canRender){
        BufferedImage image = sprite;
        g2.drawImage(image,
        Mth.lerp(gp.prevActualX+prevX,gp.actualX+x,gp.lerpProgress) - (int)(getWidth()*gp.tileSize*0.5)//스프라이트가 딱 정중앙에 오게끔 하기 위해 크기의 절반을 빼준다.
        ,Mth.lerp(gp.prevActualY+prevY,gp.actualY+y,gp.lerpProgress) - (int)(getHeight()*gp.tileSize)
        ,(int)(getWidth()*gp.tileSize),(int)(getHeight()*gp.tileSize),null);
        }
    }
}
