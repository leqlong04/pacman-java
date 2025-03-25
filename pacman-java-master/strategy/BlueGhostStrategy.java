package strategy;
import model.Ghost;
import model.Player;
import view.GameBoard;

public class BlueGhostStrategy implements GhostStrategy {
    @Override
    public char calculateNextDirection(Ghost ghost, Player pacman, GameBoard board) {
        int tileSize = ghost.tileSize;
        int pacRow = pacman.y / tileSize;
        int pacCol = pacman.x / tileSize;
        char pacDir = pacman.getDirection();
        
        // Dự báo vị trí 2 ô phía trước Pac-Man
        int projectedRow = pacRow;
        int projectedCol = pacCol;
        switch(pacDir) {
            case 'U': projectedRow = pacRow - 2; break;
            case 'D': projectedRow = pacRow + 2; break;
            case 'L': projectedCol = pacCol - 2; break;
            case 'R': projectedCol = pacCol + 2; break;
        }
        // Lấy vị trí của Red Ghost (Blinky)
        Ghost redGhost = board.getRedGhost();
        int redRow = redGhost.y / tileSize;
        int redCol = redGhost.x / tileSize;
        
        // Tính vector từ Red Ghost đến vị trí dự báo và nhân đôi
        int targetRow = redRow + 2 * (projectedRow - redRow);
        int targetCol = redCol + 2 * (projectedCol - redCol);
        targetRow = Math.max(0, Math.min(board.rowCount - 1, targetRow));
        targetCol = Math.max(0, Math.min(board.columnCount - 1, targetCol));
        
        int ghostRow = ghost.y / tileSize;
        int ghostCol = ghost.x / tileSize;
        return board.getDirectionTowards(ghostRow, ghostCol, targetRow, targetCol);
    }
}
