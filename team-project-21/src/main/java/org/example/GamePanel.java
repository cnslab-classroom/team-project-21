package org.example;

import org.example.entity.BUnit1;
import org.example.entity.BUnit2;
import org.example.entity.Dummy;
import org.example.entity.Entity;
import org.example.entity.GunMan;
import org.example.entity.HitBox;
import org.example.entity.MUnit2;
import org.example.entity.Player;
import org.example.entity.projectiles.Projectile;
import org.example.tile.TileManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePanel extends JPanel implements Runnable {
    public final int originalTileSize = 16;
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;//768
    public final int screenHeight = tileSize * maxScreenRow;//576

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();
    public CopyOnWriteArrayList<Entity> projectile_entities = new CopyOnWriteArrayList<Entity>();

    //Player player = new Player(this, keyH);

    public double lerpProgress;
    private double logicInterval, renderInterval, nextLogicUpdateTime, nextRenderTime;
    private long previousRenderTime;
    private final int FPS = 60;
    private final int LOGIC_FPS = 15;
    public int actualX=0, actualY=0, prevActualX=0,prevActualY=0;

    public GamePanel(){
        setPreferredSize(new Dimension(screenWidth,screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(keyH);
        setFocusable(true);

    }

    public void startGameThread(){
        entities.add(new Player(this, keyH));
        entities.add(new Dummy(this, 700, 100));
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    int speed = 30;
    @Override
    public void run() {
        logicInterval = 1000000000.0 / LOGIC_FPS;
        renderInterval = 1000000000.0 / FPS;
        nextLogicUpdateTime = System.nanoTime();
        nextRenderTime = System.nanoTime();

        previousRenderTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            if (currentTime >= nextLogicUpdateTime) {
                prevActualX = actualX; prevActualY = actualY;
                update();
                if(keyH.isNumberKeyJustPressed(1)){
                    entities.add(new GunMan(this, 100, 100, "player"));
                } else if(keyH.isNumberKeyJustPressed(2)){
                    entities.add(new MUnit2(this, 100, 100, "player"));
                } else if(keyH.isNumberKeyJustPressed(3)){
                    entities.add(new BUnit1(this, 100, 100, "player"));
                } else if(keyH.isNumberKeyJustPressed(4)){
                    entities.add(new BUnit2(this, 100, 100, "player"));
                } else if(keyH.isNumberKeyJustPressed(5)){
                    entities.add(new GunMan(this, 1700, 100, "enemy"));
                } else if(keyH.isNumberKeyJustPressed(6)){
                    entities.add(new MUnit2(this, 1700, 100, "enemy"));
                } else if(keyH.isNumberKeyJustPressed(7)){
                    entities.add(new BUnit1(this, 1700, 100, "enemy"));
                } else if(keyH.isNumberKeyJustPressed(8)){
                    entities.add(new BUnit2(this, 1700, 100, "enemy"));
                }
                /*if(keyH.upPressed){
                    entities.add(new MUnit2(this, 100, 100, "player"));
                }else if(keyH.downPressed){
                    entities.add(new BUnit2(this, 1700, 100, "enemy"));
                }else */
                if(keyH.rightPressed){
                    actualX-=speed;
                }else if(keyH.leftPressed){
                    actualX+=speed;
                }
                nextLogicUpdateTime += logicInterval;
            }

            if (currentTime >= nextRenderTime) {
                lerpProgress = calculateLerpProgress(currentTime, nextLogicUpdateTime, logicInterval);
                repaint();
                previousRenderTime = currentTime;
                nextRenderTime += renderInterval;
            }

            long sleepTime = Math.min((long) (nextLogicUpdateTime - currentTime), (long) (nextRenderTime - currentTime)) / 1000000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private double calculateLerpProgress(long currentTime, double nextLogicUpdateTime, double logicInterval) {
        double elapsedTime = nextLogicUpdateTime - currentTime;
        double progress = 1.0 - Math.min(1.0, Math.max(0.0, elapsedTime / logicInterval));
        return progress;
    }
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> clazz, HitBox range, Comparator<T> comparator) {
        List<T> result = new ArrayList<>();
        for (Entity entity : entities) {
            if (clazz.isInstance(entity) && entity.getHitbox().intersects(range)) {
                result.add(clazz.cast(entity));
            }
        }
        result.sort(comparator);
        return result;
    }

    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> clazz, HitBox range) {
        // 기본적으로 거리 기준으로 정렬된 엔티티 반환
        return getEntitiesOfClass(clazz, range, Comparator.comparingDouble(e -> e.getHitbox().distanceTo(range)));
    }
    public void update(){
        //player.update();

        List<Entity> allEntities = new CopyOnWriteArrayList<>(entities);
        allEntities.addAll(projectile_entities);
        for (Entity e : allEntities) {
            e.update();
        }

    }
    public void addFreshEntity(Entity entity){
        entities.add(entity);
    }
    public void addFreshEntityP(Projectile entity){
        projectile_entities.add(entity);
    }
    public void remove(Entity entity){
        entities.remove(entity);
    }
    public void removeP(Projectile entity){
        projectile_entities.remove(entity);
    }

    public BufferedImage getImage(String input) {
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream(input));
            return image;
        } catch (IOException e) {
            System.err.println("이미지 로딩 중 오류 발생: " + input);
            e.printStackTrace();
            return null;
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //player.draw(g2);
        g2.drawImage(getImage("/textures/tiles/background.png"),0,-96,screenWidth,screenHeight,null);
        entities.stream()
        .sorted(Comparator.comparingInt(Entity::getZ))
        .forEach(e -> e.draw(g2));

        projectile_entities.stream()
        .sorted(Comparator.comparingInt(Entity::getZ))
        .forEach(e -> e.draw(g2));

        g2.dispose();
    }
}
