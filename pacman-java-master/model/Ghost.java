package model;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

import strategy.GhostStrategy;

public class Ghost {
    public int x, y;
    public int velocityX = 0, velocityY = 0;
    public int tileSize;
    public Image ghostImage;
    private Image scaredGhostImage;
    public char direction;
    public int startX, startY;
    private Random random = new Random();
    public boolean isVulnerable = false;

    private int speedNormal;
    private int speedScared;
    
    // Cho thời gian trì hoãn bắt đầu đuổi (3-4 giây)
    private long spawnTime;
    private int chaseDelay; // ms

    public GhostStrategy strategy; // Chiến lược di chuyển

    public Ghost(int startX, int startY, Image ghostImage, int tileSize, GhostStrategy strategy) {
        this.startX = startX;
        this.startY = startY;
        this.x = startX;
        this.y = startY;
        this.tileSize = tileSize;
        this.ghostImage = ghostImage;
        this.scaredGhostImage = new ImageIcon(getClass().getResource("/images/scaredGhost.png")).getImage();
        this.strategy = strategy;
        
        this.speedNormal = tileSize / 8;
        this.speedScared = tileSize / 16;
        
        spawnTime = System.currentTimeMillis();
        chaseDelay = random.nextInt(1000) + 3000; // 3000 đến 4000 ms

        char[] directions = {'U', 'D', 'L', 'R'};
        direction = directions[random.nextInt(4)];
        updateVelocity();
    }

    public void updateDirection(char newDirection) {
        direction = newDirection;
        updateVelocity();
    }

    private void updateVelocity() {
        int speed = isVulnerable ? speedScared : speedNormal;
        if (direction == 'U') {
            velocityX = 0;
            velocityY = -speed;
        } else if (direction == 'D') {
            velocityX = 0;
            velocityY = speed;
        } else if (direction == 'L') {
            velocityX = -speed;
            velocityY = 0;
        } else if (direction == 'R') {
            velocityX = speed;
            velocityY = 0;
        }
    }

    public void move() {
        x += velocityX;
        y += velocityY;
    }

    public void reset() {
        x = startX;
        y = startY;
        isVulnerable = false;
        spawnTime = System.currentTimeMillis();
        chaseDelay = random.nextInt(1000) + 3000;
        updateVelocity();
    }

    public void setVulnerable(boolean vulnerable) {
        this.isVulnerable = vulnerable;
        updateVelocity();
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    public int getChaseDelay() {
        return chaseDelay;
    }

    public void draw(Graphics g) {
        if (isVulnerable) {
            g.drawImage(scaredGhostImage, x, y, tileSize, tileSize, null);
        } else {
            g.drawImage(ghostImage, x, y, tileSize, tileSize, null);
        }
    }
}
