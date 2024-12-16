package org.example.entity;

import java.awt.Graphics2D;

import org.example.GamePanel;
import org.example.utils.Mth;

import java.awt.geom.AffineTransform;
import java.util.List;

public abstract class LivingEntity extends Entity{
    private int currentHealth, attackDamage, deathTicks;
    protected boolean defaultDeathAnimation, targetFound, moving;
    private String team;
    public byte state;
    public HitBox detectRange;
    
    public LivingEntity(GamePanel gp, int x, int y, String team){
        super(gp, x, y);
        this.team = team;
        this.state = 1;
        defaultDeathAnimation = true;
        if(team=="player")
            direction = "right";
        else
            direction = "left";
        setCurrentHealth(getMaxHealth());
        detectRange = createDetectRange();
    }

    public HitBox createDetectRange() {
        return new HitBox(x, y, 0, (int)(getWidth()*gp.tileSize), getHeight()*gp.tileSize, getWidth()*gp.tileSize);
    }

    public int getCost(){
        return 0;
    }
    public void updateDetectRange() {
        // 히트박스를 현재 위치로 동기화
        int err = direction == "right" ? (int)(getWidth()*gp.tileSize)/2 : -(int)(getWidth()*gp.tileSize)/2;
        detectRange.setX(x + err);
        detectRange.setY(y);
    }
    @Override
    public void update(){
        super.update();
        if(isAlive()){
            travel();
            if(tickCount%10==5){
                updateDetectRange();
                List<LivingEntity> _entfound = gp.getEntitiesOfClass(LivingEntity.class, detectRange);
                targetFound = false;
                for(LivingEntity entityiterator : _entfound){
                    if(this!=entityiterator&&this.getTeam()!=entityiterator.getTeam()){
                        targetFound = true;
                        state = 2;
                        break;
                    }
                }
                if(!targetFound) { state = 1; }
            }
        }
        else{
            deathTicks++;
            state = 0;
            if(deathTicks > 10)
                gp.remove(this);
        }
    }
    public int getMovementSpeed(){
        return 6;
    }
    public void travel(){
        if(state == 1 && isOnGround()){
            if (direction=="right") {
                xSpeed += getMovementSpeed(); // Move right
            } else if (direction=="left") {
                xSpeed += -getMovementSpeed(); // Move left
            }
        }
    }
    public String getTeam() {
        return team;
    }
    public void setTeam(String team) {
        this.team = team;
    }
    public boolean isAlliedTo(LivingEntity entity){
        return this.team == entity.getTeam();
    }
    public int getMaxHealth(){
        return 10;
    }
    public int getCurrentHealth() {
        return currentHealth;
    }
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }
    public boolean isAlive(){
        return currentHealth > 0;
    }
    public int getAttackDamage() {
        return attackDamage;
    }
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
    @Override
    public void drawMethod(Graphics2D g2){
        if (isAlive()) {
            super.drawMethod(g2);
        } else if(defaultDeathAnimation){
            AffineTransform originalTransform = g2.getTransform();
            // Calculate position and rotation anchor
            int drawX = Mth.lerp(gp.prevActualX + prevX, gp.actualX + x, gp.lerpProgress);
            int drawY = Mth.lerp(gp.prevActualY + prevY, gp.actualY + y, gp.lerpProgress) - (int) (getHeight() * gp.tileSize);
            int width = (int) (getWidth() * gp.tileSize);
            int height = (int) (getHeight() * gp.tileSize);

            if (!isAlive() && deathTicks <= 10) {
                // Rotation logic for death
                double rotationAngle = direction.equals("right") ? Math.toRadians((deathTicks+gp.lerpProgress)*8) : Math.toRadians(-(deathTicks+gp.lerpProgress)*8);
                double pivotX = direction.equals("right") ? drawX - width / 2 : drawX + width / 2; // Pivot at the bottom-center
                double pivotY = drawY + height/2;

                g2.rotate(rotationAngle, pivotX, pivotY);
            }
            super.drawMethod(g2);
            g2.setTransform(originalTransform); // Restore original transform
        }
    }
}