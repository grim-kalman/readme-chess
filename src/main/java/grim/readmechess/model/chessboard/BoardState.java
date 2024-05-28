package grim.readmechess.model.chessboard;

import static grim.readmechess.utils.common.Constants.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
public class BoardState {

    private String activeColor;
    private String castlingAvailability;
    private String enPassantTarget;
    @Setter
    private int halfMoveClock;
    private int fullMoveNumber;

    public void reset() {
        activeColor = WHITE;
        castlingAvailability = "KQkq";
        enPassantTarget = "-";
        halfMoveClock = 0;
        fullMoveNumber = 1;
    }

    public void update(String fromSquare, boolean capture) {
        updateActiveColorAndFullMoveNumber();
        resetEnPassantTarget();
        updateCastlingRights(fromSquare);
        updateHalfMoveClock(capture);
    }

    public void handleEnPassantTarget(String toSquare) {
        String targetRank = activeColor.equals(BLACK) ? "3" : "6";
        enPassantTarget = toSquare.charAt(0) + targetRank;
    }

    private void updateActiveColorAndFullMoveNumber() {
        activeColor = activeColor.equals(BLACK) ? WHITE : BLACK;
        if (activeColor.equals(WHITE)) fullMoveNumber++;
    }

    private void resetEnPassantTarget() {
        enPassantTarget = "-";
    }

    private void updateCastlingRights(String fromSquare) {
        if (fromSquare.matches("[eah][18]")) {
            char file = fromSquare.charAt(0);
            char rank = fromSquare.charAt(1);
            castlingAvailability = castlingAvailability
                    .replace(file == 'a' ? "Q" : "K", "")
                    .replace(rank == '1' ? "Q" : "q", "");
        }
    }

    private void updateHalfMoveClock(boolean capture) {
        halfMoveClock = capture ? 0 : halfMoveClock + 1;
    }
}
