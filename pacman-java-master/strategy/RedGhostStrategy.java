package strategy;
import model.Ghost;
import model.Player;
import view.GameBoard;

public class RedGhostStrategy implements GhostStrategy {
    @Override
    public char calculateNextDirection(Ghost ghost, Player pacman, GameBoard board) {
        // Sử dụng thuật toán A* có sẵn trong GameBoard
        return board.getNextDirectionAStar(ghost);
    }
}
