package view;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Food;
import model.Ghost;
import model.Player;
import model.Wall;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PacMan extends JPanel implements ActionListener, KeyListener {
    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    
    private GameBoard gameBoard;
    private Timer gameLoop;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    
    public PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        
        gameBoard = new GameBoard(tileSize);
        
        // Khởi chạy game loop với khoảng 50ms (20 FPS)
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Vẽ tường
        for (Wall wall : gameBoard.getWalls()) {
            wall.draw(g);
        }
        // Vẽ thức ăn
        for (Food food : gameBoard.getFoods()) {
            food.draw(g);
        }
        // Vẽ ghost
        for (Ghost ghost : gameBoard.getGhosts()) {
            ghost.draw(g);
        }
        // Vẽ Pac-Man
        gameBoard.getPlayer().draw(g);
        
        // Hiển thị score và số mạng
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + score, tileSize / 2, tileSize / 2);
        } else {
            g.drawString("x" + lives + " Score: " + score, tileSize / 2, tileSize / 2);
        }
    }
    
    // Phương thức kiểm tra va chạm giữa 2 hình chữ nhật
    public boolean collision(int x1, int y1, int w1, int h1,
                             int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 &&
               y1 < y2 + h2 && y1 + h1 > y2;
    }
    
    public void moveGame() {
        Player player = gameBoard.getPlayer();
        int prevX = player.x;
        int prevY = player.y;
        player.move();

        // Kiểm tra va chạm Pac-Man với tường
        for (Wall wall : gameBoard.getWalls()) {
            if (collision(player.x, player.y, tileSize, tileSize,
                          wall.x, wall.y, tileSize, tileSize)) {
                player.x = prevX;
                player.y = prevY;
                break;
            }
        }

        // Di chuyển ghost
        long currentTime = System.currentTimeMillis();
        for (Ghost ghost : gameBoard.getGhosts()) {
            int ghostPrevX = ghost.x;
            int ghostPrevY = ghost.y;
            
            if (ghost.x % tileSize == 0 && ghost.y % tileSize == 0) {
                // Kiểm tra thời gian trì hoãn nếu cần (nếu bạn muốn ghost bắt đầu đuổi sau 3-4 giây)
                long currenTime = System.currentTimeMillis();
                if (currenTime - ghost.getSpawnTime() >= ghost.getChaseDelay()) {
                    char nextDir = ghost.strategy.calculateNextDirection(ghost, gameBoard.getPlayer(), gameBoard);
                    ghost.updateDirection(nextDir);
                }
            }
            
            ghost.move();

            // Kiểm tra va chạm ghost với tường
            for (Wall wall : gameBoard.getWalls()) {
                if (collision(ghost.x, ghost.y, tileSize, tileSize,
                              wall.x, wall.y, tileSize, tileSize)) {
                    ghost.x = ghostPrevX;
                    ghost.y = ghostPrevY;
                    char[] directions = {'U', 'D', 'L', 'R'};
                    ghost.updateDirection(directions[new java.util.Random().nextInt(4)]);
                }
            }
        }

        // Các xử lý khác của moveGame() (va chạm với ghost, ăn thức ăn, …)
        // [Phần xử lý vẫn giữ nguyên như trước...]
        
        // Kiểm tra va chạm giữa Pac-Man và ghost
        for (Ghost ghost : gameBoard.getGhosts()) {
            if (collision(player.x, player.y, tileSize, tileSize,
                          ghost.x, ghost.y, tileSize, tileSize)) {
                if (ghost.isVulnerable) {
                    ghost.reset(); // Quay lại vị trí spawn
                    score += 200;
                } else {
                    lives--;
                    if (lives == 0) {
                        gameOver = true;
                        gameLoop.stop();
                        return;
                    }
                    resetPositions();
                }
                break;
            }
        }

        // Kiểm tra ăn thức ăn
        Food foodEaten = null;
        for (Food food : gameBoard.getFoods()) {
            if (collision(player.x, player.y, tileSize, tileSize,
                          food.x, food.y, food.size, food.size)) {
                foodEaten = food;
                score += food.isPowerPellet ? 50 : 10;
                if (food.isPowerPellet) {
                    for (Ghost ghost : gameBoard.getGhosts()) {
                        ghost.setVulnerable(true);
                    }
                    new javax.swing.Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            for (Ghost ghost : gameBoard.getGhosts()) {
                                ghost.setVulnerable(false);
                            }
                            ((javax.swing.Timer) evt.getSource()).stop();
                        }
                    }).start();
                }
                break;
            }
        }
        if (foodEaten != null) {
            gameBoard.getFoods().remove(foodEaten);
        }

        // Nếu hết thức ăn, reset bản đồ
        if (gameBoard.getFoods().isEmpty()) {
            gameBoard.resetMap();
            resetPositions();
        }
    }

    
    public void resetPositions() {
        gameBoard.getPlayer().reset();
        for (Ghost ghost : gameBoard.getGhosts()) {
            ghost.reset();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveGame();
            repaint();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // Khi game over, nhấn phím sẽ reset lại game
        if (gameOver) {
            gameBoard.resetMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        Player player = gameBoard.getPlayer();
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player.updateDirection('U');
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player.updateDirection('D');
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.updateDirection('L');
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.updateDirection('R');
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
}
