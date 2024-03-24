package grim.readmechess.webapi.model.chessboard;

import grim.readmechess.webapi.model.chesspieces.Piece;
import org.springframework.stereotype.Component;

import java.util.List;

import static grim.readmechess.utils.Constants.*;

@Component
public class BoardState {
    private static final String INITIAL_CASTLING_AVAILABILITY = "KQkq";
    private static final String DEFAULT_EN_PASSANT_TARGET = "-";
    private static final int INITIAL_HALF_MOVE_CLOCK = 0;
    private static final int INITIAL_FULL_MOVE_NUMBER = 1;

    private String activeColor;
    private String castlingAvailability;
    private String enPassantTarget;
    private int halfMoveClock;
    private int fullMoveNumber;

    public BoardState() {
        this.activeColor = WHITE;
        this.castlingAvailability = INITIAL_CASTLING_AVAILABILITY;
        this.enPassantTarget = DEFAULT_EN_PASSANT_TARGET;
        this.halfMoveClock = INITIAL_HALF_MOVE_CLOCK;
        this.fullMoveNumber = INITIAL_FULL_MOVE_NUMBER;
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

    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }

    public void updateActiveColor() {
        activeColor = activeColor.equals(BLACK) ? WHITE : BLACK;
        if (activeColor.equals(WHITE)) {
            fullMoveNumber++;
        }
    }

    public void updateCastlingRights(String fromSquare) {
        switch (fromSquare) {
            case "e1" -> castlingAvailability = castlingAvailability.replace("K", "").replace("Q", "");
            case "e8" -> castlingAvailability = castlingAvailability.replace("k", "").replace("q", "");
            case "a1" -> castlingAvailability = castlingAvailability.replace("Q", "");
            case "h1" -> castlingAvailability = castlingAvailability.replace("K", "");
            case "a8" -> castlingAvailability = castlingAvailability.replace("q", "");
            case "h8" -> castlingAvailability = castlingAvailability.replace("k", "");
            default -> {
                // No action
            }
        }
    }

    public void resetEnPassantTarget() {
        enPassantTarget = DEFAULT_EN_PASSANT_TARGET;
    }

    public void updateHalfMoveClock(String toSquare, List<Piece> pieces) {
        halfMoveClock = pieces.stream()
                .filter(piece -> piece.getPosition().equals(toSquare))
                .findFirst()
                .map(piece -> {
                    pieces.remove(piece);
                    return 0;
                })
                .orElse(halfMoveClock + 1);
    }

    public void handleEnPassantTarget(String fromSquare, String toSquare) {
        if (Math.abs(fromSquare.charAt(1) - toSquare.charAt(1)) == 2) {
            enPassantTarget = toSquare.charAt(0) + (activeColor.equals(BLACK) ? "3" : "6");
        }
    }
}
