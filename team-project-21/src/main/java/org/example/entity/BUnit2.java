package org.example.entity;

import java.awt.image.BufferedImage;

import org.example.GamePanel;
import org.example.entity.projectiles.ArchBullet;

public class BUnit2 extends LivingEntity{
    int attackTicks;
    byte prevState;
    public BufferedImage[] moveSprites = new BufferedImage[8];
    public BufferedImage[] readySprites = new BufferedImage[4];
    public BufferedImage[] attackSprites = new BufferedImage[5];
    public BufferedImage[] dyingSprites = new BufferedImage[5];

    public BUnit2(GamePanel gp, int x, int y, String team){
        super(gp, x, y, team);
        setAttackDamage(7);
        moveSprites[0] = getImage("/textures/entities/big_unit2/unit4_move-1.png");
        moveSprites[1] = getImage("/textures/entities/big_unit2/unit4_move-2.png");
        moveSprites[2] = getImage("/textures/entities/big_unit2/unit4_move-3.png");
        moveSprites[3] = getImage("/textures/entities/big_unit2/unit4_move-4.png");
        moveSprites[4] = getImage("/textures/entities/big_unit2/unit4_move-5.png");
        moveSprites[5] = getImage("/textures/entities/big_unit2/unit4_move-6.png");
        moveSprites[6] = getImage("/textures/entities/big_unit2/unit4_move-7.png");
        moveSprites[7] = getImage("/textures/entities/big_unit2/unit4_move-8.png");
        attackSprites[0] = getImage("/textures/entities/big_unit2/unit4_atk-5.png");
        attackSprites[1] = getImage("/textures/entities/big_unit2/unit4_atk-6.png");
        attackSprites[2] = getImage("/textures/entities/big_unit2/unit4_atk-7.png");
        attackSprites[3] = getImage("/textures/entities/big_unit2/unit4_atk-8.png");
        attackSprites[4] = getImage("/textures/entities/big_unit2/unit4_atk-9.png");
        readySprites[0] = getImage("/textures/entities/big_unit2/unit4_atk-1.png");
        readySprites[1] = getImage("/textures/entities/big_unit2/unit4_atk-2.png");
        readySprites[2] = getImage("/textures/entities/big_unit2/unit4_atk-3.png");
        readySprites[3] = getImage("/textures/entities/big_unit2/unit4_atk-4.png");
        /*
        dyingSprites[0] = getImage("/textures/entities/gun_mecha_b_unit1_die1");
        dyingSprites[1] = getImage("/textures/entities/gun_mecha_b_unit1_die2");
        dyingSprites[2] = getImage("/textures/entities/gun_mecha_b_unit1_die3");
        dyingSprites[3] = getImage("/textures/entities/gun_mecha_b_unit1_die4");
        dyingSprites[4] = getImage("/textures/entities/gun_mecha_b_unit1_die5");
        */
    }

    @Override
    public float getWidth(){
        return 3;
    }
    @Override
    public float getHeight(){
        return 3;
    }
    public int getMovementSpeed(){
        return 16;
    }
    public void update(){
        prevState = state;
        super.update();
        if(state == 2&&prevState!=2){
            attackTicks = tickCount;
        }
        switch (state) {
            case 0 -> sprite = moveSprites[0];
            case 1 -> {
                sprite = moveSprites[tickCount%8];
            }
            case 2 -> {
                int ticks = (tickCount-attackTicks)%60; // 4초
                
                if(ticks<5){
                    sprite = attackSprites[ticks];
                    if(ticks == 1)
                    gp.addFreshEntityP(new ArchBullet(gp, x, y - (int)(getHeight() * gp.tileSize)/2, z, this, (int)Math.sqrt((double)3*Math.abs(target.x+target.xSpeed-this.x-this.xSpeed)), 80));
                }else{
                    sprite = attackSprites[0];
                }
            }
        }
    }
    public void travel(){
        int mod = tickCount & 7; // % 8 대신 비트 연산
        if (mod == 3 || mod == 7) {
            super.travel();
        }
    }
    public HitBox createDetectRange() {
        return new HitBox(x, y, z, (int)(24 * gp.tileSize), getHeight()*gp.tileSize, 10*gp.tileSize);
    }
    public int getCost(){
        return 10;
    }
    public int getMaxHealth(){
        return 110;
    }
}
