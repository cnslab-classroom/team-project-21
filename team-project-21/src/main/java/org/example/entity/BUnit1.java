package org.example.entity;

import java.awt.image.BufferedImage;

import org.example.GamePanel;
import org.example.entity.projectiles.BigBullet;

public class BUnit1 extends LivingEntity {
    int attackTicks;
    byte prevState;
    public BufferedImage[] moveSprites = new BufferedImage[4];
    public BufferedImage[] attackSprites = new BufferedImage[10];
    public BufferedImage[] dyingSprites = new BufferedImage[5];

    public BUnit1(GamePanel gp, int x, int y, String team){
        super(gp, x, y, team);
        setAttackDamage(15);
        moveSprites[0] = getImage("/textures/entities/gun_mecha/b_unit1_walk1.png");
        moveSprites[1] = getImage("/textures/entities/gun_mecha/b_unit1_walk2.png");
        moveSprites[2] = getImage("/textures/entities/gun_mecha/b_unit1_walk3.png");
        moveSprites[3] = getImage("/textures/entities/gun_mecha/b_unit1_walk4.png");
        attackSprites[0] = getImage("/textures/entities/gun_mecha/b_unit1_atk1.png");
        attackSprites[1] = getImage("/textures/entities/gun_mecha/b_unit1_atk2.png");
        attackSprites[2] = getImage("/textures/entities/gun_mecha/b_unit1_atk3.png");
        attackSprites[3] = getImage("/textures/entities/gun_mecha/b_unit1_atk4.png");
        attackSprites[4] = getImage("/textures/entities/gun_mecha/b_unit1_atk5.png");
        attackSprites[5] = getImage("/textures/entities/gun_mecha/b_unit1_atk6.png");
        attackSprites[6] = getImage("/textures/entities/gun_mecha/b_unit1_atk7.png");
        attackSprites[7] = getImage("/textures/entities/gun_mecha/b_unit1_atk8.png");
        attackSprites[8] = getImage("/textures/entities/gun_mecha/b_unit1_atk9.png");
        attackSprites[9] = getImage("/textures/entities/gun_mecha/b_unit1_atk10.png");
        dyingSprites[0] = getImage("/textures/entities/gun_mecha/b_unit1_die1.png");
        dyingSprites[1] = getImage("/textures/entities/gun_mecha/b_unit1_die2.png");
        dyingSprites[2] = getImage("/textures/entities/gun_mecha/b_unit1_die3.png");
        dyingSprites[3] = getImage("/textures/entities/gun_mecha/b_unit1_die4.png");
        dyingSprites[4] = getImage("/textures/entities/gun_mecha/b_unit1_die5.png");
    }

    @Override
    public float getWidth(){
        return 2;
    }
    @Override
    public float getHeight(){
        return 2;
    }

    public void tickLiving(){
        prevState = state;
        super.tickLiving();
        if(state == 2&&prevState!=2)
            attackTicks = tickCount;
        switch (state) {
            case 0 -> sprite = moveSprites[0];
            case 1 -> {
                sprite = moveSprites[tickCount%4];
            }
            case 2 -> {
                int ticks = (tickCount-attackTicks)%30; // 2초
                
                if(ticks<10){
                    sprite = attackSprites[ticks];
                    if(ticks == 2 || ticks == 7)
                    gp.addFreshEntityP(new BigBullet(gp, x, y - (int)(getHeight() * gp.tileSize)/4, z, this, 100, 8));
                }else{
                    sprite = moveSprites[0];
                }
            }
        }
    }
    public HitBox createDetectRange() {
        return new HitBox(x, y, z, (int)(12 * gp.tileSize), getHeight()*gp.tileSize, getHeight()*gp.tileSize);
    }
    public int getCost(){
        return 500;
    }
    public int getMaxHealth(){
        return 250;
    }
}
