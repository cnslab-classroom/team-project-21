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
    public LivingEntity target;
    
    public LivingEntity(GamePanel gp, int x, int y, String team){
        super(gp, x, y, -random.nextInt(100)-1);
        this.team = team;
        this.state = 1;
        defaultDeathAnimation = true;
        if ("player".equals(team)) {
            direction = "right";
        } else {
            direction = "left";
        }
        setCurrentHealth(getMaxHealth());
        detectRange = createDetectRange();
    }

    public HitBox createDetectRange() {
        return new HitBox(x, y, z, (int)(getWidth() * gp.tileSize * 2), (int)(getHeight() * gp.tileSize), (int)(getHeight() * gp.tileSize));
    }

    public int getCost(){
        return 0;
    }
    public void updateDetectRange() {
        // 히트박스를 현재 위치로 동기화
        /*
        int err = direction == "right" ? (int)(getWidth()*gp.tileSize)*2 : -(int)(getWidth()*gp.tileSize)*2;
        detectRange.setX(x + err);
        */
        detectRange.setX(x);
        detectRange.setY(y);
        detectRange.setZ(z);
    }
    @Override
    public void update(){
        super.update();
        if(isAlive()){
            tickLiving();
        }
        else{
            tickDeath();
        }
    }
    public void tickLiving(){
        travel();
        if(tickCount%10==5){
            updateDetectRange();
            List<LivingEntity> _entfound = gp.getEntitiesOfClass(LivingEntity.class, detectRange);
            targetFound = false;
            for(LivingEntity entityiterator : _entfound){
                if(this!=entityiterator&&entityiterator.isAlive()&&this.getTeam()!=entityiterator.getTeam()){
                    targetFound = true;
                    target = entityiterator;
                    state = 2;
                    this.direction = (entityiterator.x > this.x) ? "right" : "left";
                    break;
                }
            }
            if(!targetFound) {
                this.direction = this.team == "player" ? "right" : "left";
                state = 1; 
            } else {
                this.direction = (target.x > this.x) ? "right" : "left";
            }
        }
    }
    public void tickDeath(){
        deathTicks++;
        state = 0;
        if(deathTicks > getMaxDeathTicks())
            gp.remove(this);
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
    protected int getMaxDeathTicks(){
        return 10;
    }
    @Override
    public void drawMethod(Graphics2D g2){
        if (isAlive()) {
            super.drawMethod(g2);
        } else if(defaultDeathAnimation){
            AffineTransform originalTransform = g2.getTransform();
            double scale = 1.1 / Math.cbrt(Math.max(1, (double)-z/40));
            // Calculate position and rotation anchor
            int scaledWidth = (int) (getWidth() * gp.tileSize * scale);
            int scaledHeight = (int) (getHeight() * gp.tileSize * scale);

            // z 값을 반영한 좌표 계산
            int drawX = (int) Mth.lerp(gp.prevActualX + prevX, gp.actualX + x, gp.lerpProgress) - scaledWidth / 2;
            int drawY = (int) Mth.lerp(gp.prevActualY + prevY + prevZ, gp.actualY + y + z, gp.lerpProgress) - scaledHeight / 2;

            if (!isAlive() && deathTicks <= getMaxDeathTicks()) {
                // Rotation logic for death
                double rotationAngle = direction.equals("right") ? Math.toRadians((deathTicks+gp.lerpProgress)*8) : Math.toRadians(-(deathTicks+gp.lerpProgress)*8);
                double pivotX = drawX + scaledWidth / 2; // Pivot at the bottom-center
                double pivotY = drawY + scaledHeight / 2;

                g2.rotate(rotationAngle, pivotX, pivotY);
            }
            super.drawMethod(g2);
            g2.setTransform(originalTransform); // Restore original transform
        }
    }
}
