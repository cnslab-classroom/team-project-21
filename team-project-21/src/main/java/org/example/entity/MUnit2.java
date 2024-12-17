package org.example.entity;

import java.awt.image.BufferedImage;

import org.example.GamePanel;
import org.example.entity.projectiles.AntiairBullet;
import org.example.entity.projectiles.ArchBullet;

public class MUnit2 extends LivingEntity {
    int attackTicks;
    byte prevState;
    public BufferedImage[] moveSprites = new BufferedImage[2];
    public BufferedImage[] attackSprites = new BufferedImage[5];

    public MUnit2(GamePanel gp, int x, int y, String team){
        super(gp, x, y, team);
        setAttackDamage(40);
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
    public void tickDeath(){
        if(deathTicks > getMaxDeathTicks() - 2){
            setAttackDamage(0);
            gp.addFreshEntityP(new ArchBullet(gp, x, y, z - 1, this, 0, 2));
            Dummy rider = new Dummy(gp, x, y - (int) getHeight() * gp.tileSize, z);
            rider.direction = this.direction;
            rider.xSpeed = this.direction == "right" ? -20 : 20;
            rider.ySpeed = -40;
            rider.setCurrentHealth(0);
            gp.addFreshEntity(rider);
            gp.remove(this);
        }
        super.tickDeath();
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
                    if(ticks == 1){
                        gp.playSound("/sounds/sf/gun_man_shoot.wav");
                        gp.addFreshEntityP(new AntiairBullet(gp, x, y - (int)(getHeight() * gp.tileSize)/2, z, this, 80, 8));
                    }
                }else{
                    sprite = moveSprites[0];
                }
            }
        }
    }
    public HitBox createDetectRange() {
        return new HitBox(x, y, z, (int)(20 * gp.tileSize), getHeight()*gp.tileSize, getHeight()*gp.tileSize);
    }
    public static int getCost(){
        return 40;
    }
    public int getMaxHealth(){
        return 45;
    }
}
