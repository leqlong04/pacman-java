package strategy;
import model.Ghost;
import model.Player;
import view.GameBoard;

public class OrangeGhostStrategy implements GhostStrategy {
    @Override
    public char calculateNextDirection(Ghost ghost, Player pacman, GameBoard board) {
        int tileSize = ghost.tileSize;
        int ghostRow = ghost.y / tileSize;
        int ghostCol = ghost.x / tileSize;
        int pacRow = pacman.y / tileSize;
        int pacCol = pacman.x / tileSize;
        
        // Tính khoảng cách Manhattan (số ô)
        int distance = Math.abs(ghostRow - pacRow) + Math.abs(ghostCol - pacCol);
        if(distance > 8) {
            // Nếu xa Pac-Man, đuổi theo như ghost đỏ
            return board.getNextDirectionAStar(ghost);
        } else {
            // Nếu gần Pac-Man, nhắm về góc dưới bên trái của bản đồ
            int targetRow = board.rowCount - 1;
            int targetCol = 0;
            return board.getDirectionTowards(ghostRow, ghostCol, targetRow, targetCol);
        }
    }
}
