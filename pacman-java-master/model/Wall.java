package model;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Wall {
    public int x, y;
    public int tileSize;
    public Image wallImage;
    
    public Wall(int x, int y, int tileSize) {
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        wallImage = new ImageIcon(getClass().getResource("/images/wall.png")).getImage();
    }
    
    public void draw(Graphics g) {
        g.drawImage(wallImage, x, y, tileSize, tileSize, null);
    }
}
