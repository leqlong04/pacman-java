package strategy;
import model.Ghost;
import model.Player;
import view.GameBoard;

public interface GhostStrategy {
    // Tính toán hướng đi tiếp theo cho ghost dựa trên vị trí của Pac-Man và bản đồ.
    char calculateNextDirection(Ghost ghost, Player pacman, GameBoard board);
}
