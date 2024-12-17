package org.example.entity;

import java.awt.image.BufferedImage;

import org.example.GamePanel;
import org.example.entity.projectiles.AntiairBullet;

public class MUnit2 extends LivingEntity {
    int attackTicks;
    byte prevState;
    public BufferedImage[] moveSprites = new BufferedImage[2];
    public BufferedImage[] attackSprites = new BufferedImage[5];

    public MUnit2(GamePanel gp, int x, int y, String team){
        super(gp, x, y, team);
        moveSprites[0] = getImage("/textures/entities/middle_unit2/unit6_move-1.png");
        moveSprites[1] = getImage("/textures/entities/middle_unit2/unit6_move-2.png");
        attackSprites[0] = getImage("/textures/entities/middle_unit2/unit6_atk-1.png");
        attackSprites[1] = getImage("/textures/entities/middle_unit2/unit6_atk-2.png");
        attackSprites[2] = getImage("/textures/entities/middle_unit2/unit6_atk-3.png");
        attackSprites[3] = getImage("/textures/entities/middle_unit2/unit6_atk-4.png");
        attackSprites[4] = getImage("/textures/entities/middle_unit2/unit6_atk-5.png");
    }

    @Override
    public float getWidth(){
        return 1.5f;
    }
    @Override
    public float getHeight(){
        return 1.5f;
    }

    public void update(){
        prevState = state;
        super.update();
        if(state == 2&&prevState!=2)
            attackTicks = tickCount;
        switch (state) {
            case 0 -> sprite = moveSprites[0];
            case 1 -> {
                sprite = moveSprites[tickCount%2];
            }
            case 2 -> {
                int ticks = (tickCount-attackTicks)%120; // 8초
                
                if(ticks<5){
                    sprite = attackSprites[ticks];
                    if(ticks == 1)
                    gp.addFreshEntityP(new AntiairBullet(gp, x, y - (int)(getHeight() * gp.tileSize)/2, z, this, 80, 8));
                }else{
                    sprite = moveSprites[0];
                }
            }
        }
    }
    public HitBox createDetectRange() {
        return new HitBox(x, y, z, (int)(20 * gp.tileSize), getHeight()*gp.tileSize, getHeight()*gp.tileSize);
    }
    public int getCost(){
        return 10;
    }
    public int getMaxHealth(){
        return 45;
    }
}
