package org.example.entity;

import org.example.GamePanel;

public abstract class LivingEntity extends Entity{
    private int currentHealth, attackDamage, detectRange, deathTicks;
    
    private String team;
    
    public LivingEntity(GamePanel gp, int x, int y, String team){
        super(gp, x, y);
        this.team = team;
        setCurrentHealth(getMaxHealth());
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
        return currentHealth <= 0;
    }
    public int getAttackDamage() {
        return attackDamage;
    }
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
    public int getDetectRange() {
        return detectRange;
    }
    public void setDetectRange(int detectRange) {
        this.detectRange = detectRange;
    }
}
