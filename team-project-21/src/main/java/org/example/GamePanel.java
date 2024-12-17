package org.example;

import org.example.entity.BUnit1;
import org.example.entity.BUnit2;
import org.example.entity.CommandCenter;
import org.example.entity.Entity;
import org.example.entity.GunMan;
import org.example.entity.HitBox;
import org.example.entity.MUnit2;
import org.example.entity.SUnit2;
import org.example.entity.projectiles.Projectile;
import org.example.tile.TileManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.sound.sampled.*; // Java Sound API
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

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
    private final Map<String, Clip> soundCache = new HashMap<>();


    // 최대 동시에 재생할 수 있는 음향 효과 개수
    private static final int MAX_SOUNDS = 30;

    // 소리 재생 큐
    private final Queue<String> soundQueue = new LinkedList<>();
    private final Queue<String> ambientSoundQueue = new LinkedList<>();
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

    private int player_money;
    private int player_get_money;
    private int enemy_money;
    private int enemy_get_money;

    private double summon_period;
    private double summon_interval;
    private int enemy_rotation;

    private double money_interval;
    private double next_money;

    private int interfaceX;
    private int interfaceY;
    Font UI;

    private boolean isGameStarted = false; // 게임 시작 여부
    private boolean gameOver = false;      // 게임 종료 여부
    private String gameOverMessage = "";   // 승리 또는 패배 메시지

    public GamePanel(){
        setPreferredSize(new Dimension(screenWidth,screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(keyH);
        setFocusable(true);
        loadSoundsFromFolder("/sounds/sf");

        UI = new Font("Arial", Font.PLAIN, 20);
    }

    public void startGameThread() {
        resetGame();
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void loadSoundsFromFolder(String folderPath) {
        try {
            // 폴더 경로를 가져옴
            File folder = new File(Objects.requireNonNull(getClass().getResource(folderPath)).toURI());
    
            // 폴더 내 모든 파일 탐색
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.isFile() && isValidSoundFile(file.getName())) {
                    String soundPath = folderPath + "/" + file.getName();
                    loadSound(soundPath); // 음향 로딩 메서드 호출
                }
            }
        } catch (URISyntaxException e) {
            System.err.println("리소스 폴더 경로를 찾을 수 없습니다: " + folderPath);
            e.printStackTrace();
        }
    }

    private void loadSound(String soundPath) {
        try {
            if (!soundCache.containsKey(soundPath)) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(soundPath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                soundCache.put(soundPath, clip);
                System.out.println("Loaded sound: " + soundPath);
            }
        } catch (Exception e) {
            System.err.println("음향 파일 로딩 실패: " + soundPath);
            e.printStackTrace();
        }
    }
    
    private boolean isValidSoundFile(String fileName) {
        // 유효한 음향 파일 확장자 필터링
        return fileName.endsWith(".wav") || fileName.endsWith(".mp3") || fileName.endsWith(".ogg");
    }
    

    public void playSound(String soundPath) {
        if (soundQueue.size() < MAX_SOUNDS) {
            soundQueue.offer(soundPath);
        }
    }
    public void playAmbientSound(String soundPath) {
        if (soundQueue.size() + ambientSoundQueue.size() < MAX_SOUNDS) {
            ambientSoundQueue.offer(soundPath);
        }
    }

    private void processSoundQueue() {
        int totalPlayedSounds = 0;
    
        // 일반 음향 효과 재생
        while (!soundQueue.isEmpty() && totalPlayedSounds < MAX_SOUNDS) {
            String soundPath = soundQueue.poll();
            playAudio(soundPath);
            totalPlayedSounds++;
        }
    
        // 앰비언트 음향 효과 재생
        while (!ambientSoundQueue.isEmpty() && totalPlayedSounds < MAX_SOUNDS) {
            String soundPath = ambientSoundQueue.poll();
            playAudio(soundPath);
            totalPlayedSounds++;
        }
    }
    
    private void playAudio(String soundPath) {
        try {
            // 오디오 스트림 열기
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(soundPath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("음향 재생 실패: " + soundPath);
            e.printStackTrace();
        }
    }

    int speed = 30;

    private void resetGame() {
        isGameStarted = false;
        gameOver = false;
        gameOverMessage = "";
        entities.clear();
        projectile_entities.clear();
        entities.add(new CommandCenter(this, 100, 100, "player"));
        entities.add(new CommandCenter(this, 1700, 100, "enemy"));
        System.out.println("Game Reset!");
    }
    public void onGameOver(String message) {
        this.gameOver = true;
        this.gameOverMessage = message;
        repaint();
    }
    @Override
    public void run() {
        logicInterval = 1000000000.0 / LOGIC_FPS;
        renderInterval = 1000000000.0 / FPS;
        nextLogicUpdateTime = System.nanoTime();
        nextRenderTime = System.nanoTime();

        previousRenderTime = System.nanoTime();

        player_money = 100;
        enemy_money = 100;

        player_get_money = 10;
        enemy_get_money = 10;

        summon_interval = logicInterval * 4;
        summon_period = System.nanoTime() + summon_interval;

        enemy_rotation = 1;

        money_interval = logicInterval * 4;
        next_money = System.nanoTime() + money_interval;

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            if (!isGameStarted) {
                if (keyH.spacePressed) { // 스페이스 키로 게임 시작
                    isGameStarted = true;
                    System.out.println("Game Started!");
                }
            } else if (gameOver) {
                // 게임 종료 후 R키를 누르면 재시작
                if (keyH.spacePressed) { // 0번 키를 재시작 키로 사용
                    resetGame();
                }
            } else {
                if (currentTime >= nextLogicUpdateTime) {
                    prevActualX = actualX; prevActualY = actualY;
                    update();
                    if(keyH.isNumberKeyJustPressed(1) && player_money >= GunMan.getCost()){
                        entities.add(new GunMan(this, 100, 400, "player"));
                        player_money -= GunMan.getCost();
                    } else if(keyH.isNumberKeyJustPressed(2) && player_money >= SUnit2.getCost()){
                        entities.add(new SUnit2(this, 100, 400, "player"));
                        player_money -= SUnit2.getCost();
                    } else if(keyH.isNumberKeyJustPressed(3) && player_money >= MUnit2.getCost()){
                        entities.add(new MUnit2(this, 100, 400, "player"));
                        player_money -= MUnit2.getCost();
                    } else if(keyH.isNumberKeyJustPressed(4) && player_money >= BUnit1.getCost()){
                        entities.add(new BUnit1(this, 100, 400, "player"));
                        player_money -= BUnit1.getCost();
                    } else if(keyH.isNumberKeyJustPressed(5) && player_money >= BUnit2.getCost()){
                        entities.add(new BUnit2(this, 100, 400, "player"));
                        player_money -= BUnit2.getCost();
                    } /*else if(keyH.isNumberKeyJustPressed(5)){
                        entities.add(new GunMan(this, 1700, 400, "enemy"));
                    } else if(keyH.isNumberKeyJustPressed(6)){
                        entities.add(new MUnit2(this, 1700, 400, "enemy"));
                    } else if(keyH.isNumberKeyJustPressed(7)){
                        entities.add(new BUnit1(this, 1700, 400, "enemy"));
                    } else if(keyH.isNumberKeyJustPressed(8)){
                        entities.add(new BUnit2(this, 1700, 400, "enemy"));
                    }*/
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

                    if(currentTime >= next_money){
                        player_money += player_get_money;
                        enemy_money += enemy_get_money;
                        next_money += money_interval;
                    }

                    if(currentTime >= summon_period){
                        if(enemy_rotation == 1 
                        /*&& enemy_money >= GunMan.getCost()*5 + SUnit2.getCost()*3*/){
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            entities.add(new SUnit2(this, 1700, 400, "enemy"));
                            entities.add(new SUnit2(this, 1700, 400, "enemy"));
                            entities.add(new SUnit2(this, 1700, 400, "enemy"));
                            //enemy_money -= GunMan.getCost()*5 + SUnit2.getCost()*3;
                            enemy_rotation++;
                        } else if(enemy_rotation == 2 
                        /*&& enemy_money >= SUnit2.getCost()*4 + BUnit1.getCost()*3*/){
                            entities.add(new SUnit2(this, 1700, 400, "enemy"));
                            entities.add(new SUnit2(this, 1700, 400, "enemy"));
                            entities.add(new SUnit2(this, 1700, 400, "enemy"));
                            entities.add(new SUnit2(this, 1700, 400, "enemy"));
                            entities.add(new BUnit1(this, 1700, 400, "enemy"));
                            entities.add(new BUnit1(this, 1700, 400, "enemy"));
                            entities.add(new BUnit1(this, 1700, 400, "enemy"));
                            //enemy_money -= SUnit2.getCost()*4 + BUnit1.getCost()*3;
                            enemy_rotation++;
                        } else if(enemy_rotation == 3 
                        /*&& enemy_money >= GunMan.getCost()*5 + MUnit2.getCost()*2*/){
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            entities.add(new MUnit2(this, 1700, 400, "enemy"));
                            entities.add(new MUnit2(this, 1700, 400, "enemy"));
                            //enemy_money -= GunMan.getCost()*5 + MUnit2.getCost()*2;
                            enemy_rotation++;
                        } else if(enemy_rotation == 4 
                        /*&& enemy_money >= GunMan.getCost()*/){
                            entities.add(new BUnit2(this, 1700, 400, "enemy"));
                            entities.add(new BUnit2(this, 1700, 400, "enemy"));
                            entities.add(new BUnit2(this, 1700, 400, "enemy"));
                            entities.add(new BUnit2(this, 1700, 400, "enemy"));
                            //enemy_money -= BUnit2.getCost()*4;
                            enemy_rotation++;
                        } else if(enemy_rotation == 5 
                        /*&& enemy_money >= GunMan.getCost()*/){
                            entities.add(new GunMan(this, 1700, 400, "enemy"));
                            //enemy_money -= GunMan.getCost();
                            enemy_rotation = 1;
                        }
                        summon_period += summon_interval*30;
                    }

                    nextLogicUpdateTime += logicInterval;
                }
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

        processSoundQueue();

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
        if (!isGameStarted) {
            g2.setColor(Color.BLUE);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            int msgWidth = g2.getFontMetrics().stringWidth("Press space bar to START");
            g2.drawString("Press space bar to START", (screenWidth - msgWidth) / 2, screenHeight / 2);
        }
        else if(!gameOver) {
            // 2단계: 그림자 그리기
            for (Entity e : entities) {
                e.drawShadow(g2);
            }

            // 3단계: 본체 그리기
            for (Entity e : projectile_entities) {
                e.drawShadow(g2);
            }
            entities.stream()
            .sorted(Comparator.comparingInt(Entity::getZ))
            .forEach(e -> e.draw(g2));

            projectile_entities.stream()
            .sorted(Comparator.comparingInt(Entity::getZ))
            .forEach(e -> e.draw(g2));

            g2.setFont(UI);
            g2.setColor(Color.white);
            interfaceX = 0;
            interfaceY = screenHeight - 60;
        }
        else {
            // 게임 오버 화면 그리기
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            int msgWidth = g2.getFontMetrics().stringWidth(gameOverMessage);
            g2.drawString(gameOverMessage, (screenWidth - msgWidth) / 2, screenHeight / 2);
            g2.setFont(new Font("Arial", Font.PLAIN, 24));
            g2.drawString("Press space bar to restart...", screenWidth / 2 - 150, screenHeight / 2 + 50);
        }

        g2.drawString("Money = " + player_money, interfaceX, interfaceY);

        g2.dispose();
    }
}
