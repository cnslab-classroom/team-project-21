package org.example.entity;

import java.awt.image.BufferedImage;

import org.example.GamePanel;
import org.example.entity.projectiles.AppleProjectile;

public class GunMan extends LivingEntity{
    int attackTicks;
    byte prevState;
    public BufferedImage[] moveSprites = new BufferedImage[4];
    public BufferedImage[] attackSprites = new BufferedImage[2];

    public GunMan(GamePanel gp, int x, int y, String team){
        super(gp, x, y, team);
        moveSprites[0] = getImage("/textures/entities/gun_man/gun_man_idle.png");
        moveSprites[1] = getImage("/textures/entities/gun_man/gun_man_move1.png");
        moveSprites[2] = getImage("/textures/entities/gun_man/gun_man_idle.png");
        moveSprites[3] = getImage("/textures/entities/gun_man/gun_man_move2.png");
        attackSprites[0] = getImage("/textures/entities/gun_man/gun_man_attack1.png");
        attackSprites[1] = getImage("/textures/entities/gun_man/gun_man_attack2.png");
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
                int ticks = (tickCount-attackTicks)%2;
                sprite = attackSprites[ticks];
                if(ticks==0)
                    gp.addFreshEntityP(new AppleProjectile(gp, x, y - (int)(getHeight() * gp.tileSize)/2, this, 60, 20));
            }
        }
    }
    public HitBox createDetectRange() {
        return new HitBox(x, y, 0, (int)(getWidth()*gp.tileSize*20), getHeight()*gp.tileSize, getWidth()*gp.tileSize);
    }
    public int getCost(){
        return 10;
    }
    public int getMaxHealth(){
        return 20;
    }
}
