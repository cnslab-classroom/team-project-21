package org.example.entity;

import java.awt.image.BufferedImage;
import java.util.List;

import org.example.GamePanel;
import org.example.entity.projectiles.ArchBullet;

public class BUnit2 extends LivingEntity{
    int attackTicks;
    byte prevState;
    public BufferedImage[] moveSprites = new BufferedImage[8];
    public BufferedImage[] readySprites = new BufferedImage[4];
    public BufferedImage[] attackSprites = new BufferedImage[5];
    public BufferedImage[] dyingSprites = new BufferedImage[6];

    public BUnit2(GamePanel gp, int x, int y, String team){
        super(gp, x, y, team);
        setAttackDamage(7);
        defaultDeathAnimation = false;
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
        dyingSprites[0] = getImage("/textures/entities/big_unit2/unit4_death-1.png");
        dyingSprites[1] = getImage("/textures/entities/big_unit2/unit4_death-2.png");
        dyingSprites[2] = getImage("/textures/entities/big_unit2/unit4_death-3.png");
        dyingSprites[3] = getImage("/textures/entities/big_unit2/unit4_death-4.png");
        dyingSprites[4] = getImage("/textures/entities/big_unit2/unit4_death-5.png");
        dyingSprites[5] = getImage("/textures/entities/big_unit2/unit4_death-6.png");
    }

    @Override
    public float getWidth(){
        return 2;
    }
    @Override
    public float getHeight(){
        return 2;
    }
    public int getMovementSpeed(){
        return 16;
    }
    public void tickLiving(){
        prevState = state;
        super.tickLiving();
        if(state == 2&&prevState!=2){
            attackTicks = tickCount;
        }
        switch (state) {
            case 0 -> sprite = moveSprites[0];
            case 1 -> {
                sprite = moveSprites[tickCount%8];
            }
            case 2 -> {
                int ticks = ((tickCount-attackTicks)%20)/2;
                
                if(ticks<4){
                    sprite = readySprites[ticks];
                } else{
                    sprite = attackSprites[0];
                    if(ticks>8) {
                        state = 3;
                        attackTicks = tickCount;
                    }
                }
            }
            case 3 -> {
                int ticks = (tickCount-attackTicks)%60; // 4초
                
                if(ticks<5){
                    sprite = attackSprites[ticks];
                    if(ticks == 1){
                        gp.playSound("/sounds/sf/cannon_shoot.wav");
                        gp.addFreshEntityP(new ArchBullet(gp, x, y - (int)(getHeight() * gp.tileSize * 0.66), z, this, (int)Math.sqrt((double)3*Math.abs(target.x-this.x+target.xSpeed*80 - 48)), 80));
                    }
                }else{
                    sprite = attackSprites[0];
                }
            }
            case 4 -> {
                int ticks = ((tickCount-attackTicks)%20)/2;
                
                if(ticks<4){
                    sprite = readySprites[3 - ticks];
                } else{
                    sprite = moveSprites[0];
                    if(ticks>8){
                        state = 1;
                    }
                }
            }
        }
    }
    public void findTarget(){
        updateDetectRange();
            List<LivingEntity> _entfound = gp.getEntitiesOfClass(LivingEntity.class, detectRange);
            targetFound = false;
            for(LivingEntity entityiterator : _entfound){
                if(this!=entityiterator&&entityiterator.isAlive()&&this.getTeam()!=entityiterator.getTeam()){
                    targetFound = true;
                    target = entityiterator;
                    state = (state == 3) ? (byte) 3 : 2;
                    this.direction = (entityiterator.x > this.x) ? "right" : "left";
                    break;
                }
            }
            if(!targetFound) {
                this.direction = this.getTeam() == "player" ? "right" : "left";
                state = (state == 3) ? (byte) 4 : 1;
                if(state == 4)
                    attackTicks = tickCount;
            } else {
                this.direction = (target.x > this.x) ? "right" : "left";
            }
    }
    public void tickDeath(){
        sprite = dyingSprites[Math.max(0,(deathTicks-1)/2)];
        if(deathTicks > getMaxDeathTicks() - 1){
            setAttackDamage(0);
            gp.addFreshEntityP(new ArchBullet(gp, x, y, z - 1, this, 0, 2){
                public float getWidth(){
                    return super.getWidth() * 2;
                }
                public float getHeight(){
                    return super.getHeight() * 2;
                }
            });
            gp.remove(this);
        }
        super.tickDeath();
    }
    public int getMaxDeathTicks(){
        return 12;
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
    public static int getCost(){
        return 450;
    }
    public int getMaxHealth(){
        return 110;
    }
}
