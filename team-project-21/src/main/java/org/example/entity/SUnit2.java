package org.example.entity;

import java.awt.image.BufferedImage;

import org.example.GamePanel;
import org.example.entity.projectiles.Bullet;

public class SUnit2 extends LivingEntity{
    int attackTicks;
    byte prevState;
    public BufferedImage[] moveSprites = new BufferedImage[4];
    public BufferedImage[] attackSprites = new BufferedImage[4];

    public SUnit2(GamePanel gp, int x, int y, String team){
        super(gp, x, y, team);
        setAttackDamage(10);
        moveSprites[0] = getImage("/textures/entities/shield_man/shield_man_idle.png");
        moveSprites[1] = getImage("/textures/entities/shield_man/shield_man_move1.png");
        moveSprites[2] = moveSprites[0];
        moveSprites[3] = getImage("/textures/entities/shield_man/shield_man_move2.png");
        attackSprites[0] = getImage("/textures/entities/shield_man/shield_man_attack1.png");
        attackSprites[1] = moveSprites[0];
        attackSprites[2] = getImage("/textures/entities/shield_man/shield_man_attack2.png");
        attackSprites[3] = getImage("/textures/entities/shield_man/shield_man_attack3.png");
    }

    public void update(){
        prevState = state;
        super.update();
        if(state == 2&&prevState!=2)
            attackTicks = tickCount;
        switch (state) {
            case 0 -> sprite = moveSprites[0];
            case 1 -> {
                sprite = moveSprites[tickCount%4];
            }
            case 2 -> {
                int ticks = (tickCount-attackTicks)%15; // 1초
                
                if(ticks<4){
                    sprite = attackSprites[ticks];
                    if(ticks == 2){
                        gp.playSound("/sounds/sf/shield_man_attack.wav");
                        gp.addFreshEntityP(new Bullet(gp, x, y - (int)(getHeight() * gp.tileSize)/2, z, this, 16, 2){
                            public boolean canRender(){
                                return false;
                            }
                        });
                    }
                }else{
                    sprite = moveSprites[0];
                }
            }
        }
    }
    public HitBox createDetectRange() {
        return new HitBox(x, y, z, (int)(gp.tileSize), getHeight()*gp.tileSize, getHeight()*gp.tileSize);
    }
    public int getCost(){
        return 10;
    }
    public int getMaxHealth(){
        return 35;
    }
}