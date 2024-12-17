package org.example.entity;

import org.example.GamePanel;
import org.example.utils.Mth;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

public abstract class Entity {
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();
    public static final Random random = new Random();
    public GamePanel gp;
    public int x,prevX,y,prevY,z,prevZ,screenX,screenY;
    public int xSpeed, ySpeed, zSpeed;
    public int tickCount = 0;
    public BufferedImage sprite;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public boolean noCulling, hasGravity;
    private HitBox hitbox;

    public int getZ(){
        return z;
    }


    public Entity(GamePanel gp, int x, int y){
        this.gp = gp;
        noCulling = false;
        hasGravity = true;
        xSpeed = ySpeed = zSpeed = 0;
        direction = "right";
        locate(x, y);
        this.hitbox = createHitbox();
    }
    public HitBox createHitbox() {
        return new HitBox(x, y, z, (int)(getWidth() * gp.tileSize), (int)(getHeight() * gp.tileSize), (int)(getHeight() * gp.tileSize));
    }
    public HitBox getHitbox() {
        return hitbox;
    }

    public void updateHitbox() {
        // 히트박스를 현재 위치로 동기화
        hitbox.setX(x);
        hitbox.setY(y);
        hitbox.setZ(z);
    }

    public void setHitbox(HitBox hitbox) {
        this.hitbox = hitbox;
    }
    
    public void setImage(String input){
        try{
            sprite = ImageIO.read(getClass().getResourceAsStream(input));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public BufferedImage getImage(String input) {
        if (!imageCache.containsKey(input)) {
            try {
                BufferedImage image = ImageIO.read(getClass().getResourceAsStream(input));
                imageCache.put(input, image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageCache.get(input);
    }

    public void update(){
        prevX = x; prevY = y; prevZ = z;
        //tickCount++;
        tickCount = (tickCount + 1) & 0x7FFFFFFF;
        if(hasGravity && !isOnGround()){
            ySpeed += getGravitySpeed();
        }
        push(xSpeed, ySpeed);
        //tickCount = (tickCount + 1) & 0x7FFFFFFF;

        updateHitbox();
    };

    int getGravitySpeed(){
        return 6;
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
            y = 450;
            ySpeed = 0;
            xSpeed *= 0.5;
            zSpeed *= 0.5;
        }
    }
    public void push(int vectorX, int vectorY, int vectorZ){
        this.z += vectorZ;
        push(vectorX, vectorY);
    }
    public boolean isOnGround(){
        return y>=450;
    }
    
    
    
    public float getWidth(){
        return 1;
    }

    //엔티티의 겉보기 높이값 설정
    public float getHeight(){
        return 1;
    }
    public boolean canRender(){
        return noCulling || !(gp.actualX + x + getWidth() * gp.tileSize/2 < 0 ||
        gp.actualX + x - getWidth() * gp.tileSize > gp.screenWidth ||
        gp.actualY + y + getHeight() * gp.tileSize < 0 ||  // 위쪽 화면 바깥
        gp.actualY + y - getHeight() * gp.tileSize > gp.screenHeight);
    }
    public void draw(Graphics2D g2){
        if(canRender()){
            drawMethod(g2);
        }
    }
    protected void drawMethod(Graphics2D g2){
        BufferedImage image = sprite;
        // 원근법에 따라 z 값 기반 스케일 계산
        double scale = 1.1 / Math.cbrt(Math.max(1, (double)-z/40));
        int scaledWidth = (int) (getWidth() * gp.tileSize * scale);
        int scaledHeight = (int) (getHeight() * gp.tileSize * scale);

        // z 값을 반영한 좌표 계산
        int drawX = (int) Mth.lerp(gp.prevActualX + prevX, gp.actualX + x, gp.lerpProgress) - scaledWidth / 2;
        int drawY = (int) Mth.lerp(gp.prevActualY + prevY + prevZ, gp.actualY + y + z, gp.lerpProgress) - scaledHeight;

        // 이미지 그리기
        if (direction.equals("right")) {
            g2.drawImage(image, drawX, drawY, scaledWidth, scaledHeight, null);
        } else if (direction.equals("left")) {
            g2.drawImage(image, drawX + scaledWidth, drawY, -scaledWidth, scaledHeight, null);
        }
    }
}
