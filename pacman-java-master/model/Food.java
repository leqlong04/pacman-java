package model;
import java.awt.Color;
import java.awt.Graphics;

public class Food {
    public int x, y, size;
    public boolean isPowerPellet;

    public Food(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.isPowerPellet = (size == 8); // Assuming size 8 is power pellet
    }

    public void draw(Graphics g) {
        g.setColor(isPowerPellet ? Color.YELLOW : Color.WHITE);
        g.fillOval(x, y, size, size); // Use oval for classic look
    }
}
