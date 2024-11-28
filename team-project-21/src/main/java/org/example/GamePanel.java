package org.example;

import org.example.entity.AppleProjectile;
import org.example.entity.Entity;
import org.example.entity.Player;
import org.example.tile.TileManager;

import javax.swing.*;
import java.awt.*;
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
    //Player player = new Player(this, keyH);

    public double lerpProgress;
    private double logicInterval, renderInterval, nextLogicUpdateTime, nextRenderTime;
    private long previousRenderTime;
    private final int FPS = 60;
    private final int LOGIC_FPS = 20;

    public GamePanel(){
        setPreferredSize(new Dimension(screenWidth,screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(keyH);
        setFocusable(true);

    }

    public void startGameThread(){
        entities.add(new Player(this, keyH));
        entities.add(new AppleProjectile(this, "down",100,100));
        gameThread = new Thread(this);
        gameThread.start();
    }
    /*
    @Override
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null){
            currentTime = System.nanoTime();
            delta +=(currentTime - lastTime)/drawInterval;
            lastTime = currentTime;

            if(delta>=1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    */
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
                update();
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

    public void update(){
        //player.update();

        for(Entity e:entities){
            e.update();
        }

    }
    public void addFreshEntity(Entity entity){
        entities.add(entity);
    }
    public void remove(Entity entity){
        entities.remove(entity);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2);
        //player.draw(g2);

        for(Entity e:entities){
            e.draw(g2);
        }

        g2.dispose();
    }
}
