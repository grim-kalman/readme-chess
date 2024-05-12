package grim.readmechess.model.chessboard;

import org.springframework.stereotype.Component;

import static grim.readmechess.utils.common.Constants.*;

@Component
public class BoardState {

    private String activeColor;
    private String castlingAvailability;
    private String enPassantTarget;
    private int halfMoveClock;
    private int fullMoveNumber;

    public BoardState() {
        reset();
    }

    public void reset() {
        this.activeColor = WHITE;
        this.castlingAvailability = "KQkq";
        this.enPassantTarget = "-";
        this.halfMoveClock = 0;
        this.fullMoveNumber = 1;
    }

    public String getActiveColor() {
        return activeColor;
    }

    public String getCastlingAvailability() {
        return castlingAvailability;
    }

    public String getEnPassantTarget() {
        return enPassantTarget;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public void setHalfMoveClock(int clock) {
        this.halfMoveClock = clock;
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
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
