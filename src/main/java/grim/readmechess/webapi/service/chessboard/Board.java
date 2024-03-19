package grim.readmechess.webapi.service.chessboard;

import grim.readmechess.webapi.service.chesspieces.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static grim.readmechess.utils.Constants.*;

@Component
public class Board {
    public static final int SQUARE_SIZE = 40;
    private static final String INITIAL_CASTLING_AVAILABILITY = "KQkq";
    private static final String DEFAULT_EN_PASSANT_TARGET = "-";
    private static final int INITIAL_HALF_MOVE_CLOCK = 0;
    private static final int INITIAL_FULL_MOVE_NUMBER = 1;

    private List<Piece> pieces;
    private String activeColor;
    private String castlingAvailability;
    private String enPassantTarget;
    private int halfMoveClock;
    private int fullMoveNumber;

    public Board() {
        this.pieces = setupStartingPosition();
        this.activeColor = WHITE;
        this.castlingAvailability = INITIAL_CASTLING_AVAILABILITY;
        this.enPassantTarget = DEFAULT_EN_PASSANT_TARGET;
        this.halfMoveClock = INITIAL_HALF_MOVE_CLOCK;
        this.fullMoveNumber = INITIAL_FULL_MOVE_NUMBER;
    }

    public List<Piece> getPieces() {
        return pieces;
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

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }

    private List<Piece> setupStartingPosition() {
        List<Piece> startingPieces = new ArrayList<>();
        startingPieces.add(new King(WHITE, "e1"));
        startingPieces.add(new King(BLACK, "e8"));
        startingPieces.add(new Rook(WHITE, "a1"));
        startingPieces.add(new Rook(WHITE, "h1"));
        startingPieces.add(new Rook(BLACK, "a8"));
        startingPieces.add(new Rook(BLACK, "h8"));
        startingPieces.add(new Queen(WHITE, "d1"));
        startingPieces.add(new Queen(BLACK, "d8"));
        startingPieces.add(new Knight(WHITE, "b1"));
        startingPieces.add(new Knight(WHITE, "g1"));
        startingPieces.add(new Knight(BLACK, "b8"));
        startingPieces.add(new Knight(BLACK, "g8"));
        startingPieces.add(new Bishop(WHITE, "c1"));
        startingPieces.add(new Bishop(WHITE, "f1"));
        startingPieces.add(new Bishop(BLACK, "c8"));
        startingPieces.add(new Bishop(BLACK, "f8"));
        for (char col = 'a'; col <= 'h'; col++) {
            startingPieces.add(new Pawn(WHITE, "" + col + '2'));
            startingPieces.add(new Pawn(BLACK, "" + col + '7'));
        }
        return startingPieces;
    }

    public void makeMove(String move) {
        updateActiveColor();
        resetEnPassantTarget();
        String fromSquare = move.substring(0, 2);
        String toSquare = move.substring(2, 4);
        updateCastlingRights(fromSquare);
        updateHalfMoveClock(toSquare);
        movePiece(fromSquare, toSquare);
    }

    private void updateActiveColor() {
        if (activeColor.equals(BLACK)) {
            activeColor = WHITE;
            fullMoveNumber++;
        } else {
            activeColor = BLACK;
        }
    }

    private void updateCastlingRights(String fromSquare) {
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

    private void resetEnPassantTarget() {
        enPassantTarget = DEFAULT_EN_PASSANT_TARGET;
    }

    private void updateHalfMoveClock(String toSquare) {
        halfMoveClock = pieces.stream()
                .filter(piece -> piece.getPosition().equals(toSquare))
                .findFirst()
                .map(piece -> {
                    pieces.remove(piece);
                    return 0;
                })
                .orElse(halfMoveClock + 1);
    }

    private void movePiece(String fromSquare, String toSquare) {
        pieces.stream()
                .filter(piece -> piece.getPosition().equals(fromSquare))
                .findFirst()
                .ifPresent(piece -> {
                    piece.setPosition(toSquare);
                    if (piece instanceof Pawn) {
                        halfMoveClock = 0;
                        handleEnPassantTarget(fromSquare, toSquare);
                    }
                });
    }

    private void handleEnPassantTarget(String fromSquare, String toSquare) {
        if (Math.abs(fromSquare.charAt(1) - toSquare.charAt(1)) == 2) {
            enPassantTarget = toSquare.charAt(0) + (activeColor.equals(BLACK) ? "3" : "6");
        }
    }
}
