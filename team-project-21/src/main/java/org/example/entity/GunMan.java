package org.example.entity;

import java.awt.image.BufferedImage;

import org.example.GamePanel;
import org.example.entity.projectiles.Bullet;

public class GunMan extends LivingEntity{
    int attackTicks;
    byte prevState;
    public BufferedImage[] moveSprites = new BufferedImage[4];
    public BufferedImage[] attackSprites = new BufferedImage[2];

    public GunMan(GamePanel gp, int x, int y, String team){
        super(gp, x, y, team);
        setAttackDamage(2);
        moveSprites[0] = getImage("/textures/entities/gun_man/gun_man_idle.png");
        moveSprites[1] = getImage("/textures/entities/gun_man/gun_man_move1.png");
        moveSprites[2] = moveSprites[0];
        moveSprites[3] = getImage("/textures/entities/gun_man/gun_man_move2.png");
        attackSprites[0] = getImage("/textures/entities/gun_man/gun_man_attack1.png");
        attackSprites[1] = getImage("/textures/entities/gun_man/gun_man_attack2.png");
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
                int ticks = (tickCount-attackTicks)%15; // 2초
                
                if(ticks<6){
                    int t2 = ticks%2;
                    sprite = attackSprites[t2];
                    if(t2 == 0){
                        gp.playSound("/sounds/sf/gun_man_shoot.wav");
                        gp.addFreshEntityP(new Bullet(gp, x, y - (int)(getHeight() * gp.tileSize)/2, z, this, 60, 10));
                    }
                }else{
                    sprite = moveSprites[0];
                }
            }
        }
    }
    public void tickDeath(){
        if(deathTicks == 0)
            gp.playSound("/sounds/sf/man_death" + (random.nextInt(3) + 1) +".wav");
        super.tickDeath();
    }
    public HitBox createDetectRange() {
        return new HitBox(x, y, z, (int)(17*gp.tileSize), getHeight()*gp.tileSize, getHeight()*gp.tileSize);
    }
    public static int getCost(){
        return 10;
    }
    public int getMaxHealth(){
        return 20;
    }
}
