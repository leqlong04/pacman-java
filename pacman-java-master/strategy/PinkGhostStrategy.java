package strategy;
import model.Ghost;
import model.Player;
import view.GameBoard;

public class PinkGhostStrategy implements GhostStrategy {
    @Override
    public char calculateNextDirection(Ghost ghost, Player pacman, GameBoard board) {
        int tileSize = ghost.tileSize;
        int ghostRow = ghost.y / tileSize;
        int ghostCol = ghost.x / tileSize;
        int pacRow = pacman.y / tileSize;
        int pacCol = pacman.x / tileSize;
        char pacDir = pacman.getDirection(); // Phương thức này cần được thêm vào Player

        // Tính vị trí mục tiêu: 4 ô phía trước Pac-Man
        int targetRow = pacRow;
        int targetCol = pacCol;
        switch(pacDir) {
        	case 'U':
	            targetRow = pacRow - 4;
	            targetCol = Math.max(0, pacCol - 1); // Thêm giới hạn
	            break;
            case 'D':
                targetRow = pacRow + 4;
                break;
            case 'L':
                targetCol = pacCol - 4;
                break;
            case 'R':
                targetCol = pacCol + 4;
                break;
        }
        // Giới hạn vị trí mục tiêu trong phạm vi bản đồ
        targetRow = Math.max(0, Math.min(board.rowCount - 1, targetRow));
        targetCol = Math.max(0, Math.min(board.columnCount - 1, targetCol));

        return board.getDirectionTowards(ghostRow, ghostCol, targetRow, targetCol);
    }
}
