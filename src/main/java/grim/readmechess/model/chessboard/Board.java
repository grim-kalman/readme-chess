package grim.readmechess.model.chessboard;

import grim.readmechess.model.chesspieces.*;
import grim.readmechess.utils.validator.MoveValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static grim.readmechess.utils.common.Constants.BLACK;
import static grim.readmechess.utils.common.Constants.WHITE;

@Component
public class Board {

    private final BoardState boardState;
    private final Map<String, Piece> pieces;
    private final MoveValidator moveValidator;
    private String selectedSquare;

    public Board(BoardState boardState, MoveValidator moveValidator) {
        this.boardState = boardState;
        this.pieces = setupStartingPosition();
        this.moveValidator = moveValidator;
    }

    public String getSelectedSquare() {
        return selectedSquare;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public Map<String, Piece> getPieces() {
        return pieces;
    }

    public void selectSquare(String square) {
        this.selectedSquare = square.equals(this.selectedSquare) ? null : square;
    }

    public void makeMove(String move) {
        validateMove(move);
        handleMove(move);
        updateBoardState(extractToSquare(move));
    }

    private Map<String, Piece> setupStartingPosition() {
        Map<String, Piece> startingPieces = new HashMap<>();
        addPieces(startingPieces);
        return startingPieces;
    }

    private void addPieces(Map<String, Piece> pieces) {
        addPawns(pieces);
        addRooks(pieces);
        addKnights(pieces);
        addBishops(pieces);
        addRoyals(pieces);
    }

    private void addPawns(Map<String, Piece> pieces) {
        for (char col = 'a'; col <= 'h'; col++) {
            pieces.put("" + col + '2', new Pawn(WHITE));
            pieces.put("" + col + '7', new Pawn(BLACK));
        }
    }

    private void addRooks(Map<String, Piece> pieces) {
        pieces.put("a1", new Rook(WHITE));
        pieces.put("h1", new Rook(WHITE));
        pieces.put("a8", new Rook(BLACK));
        pieces.put("h8", new Rook(BLACK));
    }

    private void addKnights(Map<String, Piece> pieces) {
        pieces.put("b1", new Knight(WHITE));
        pieces.put("g1", new Knight(WHITE));
        pieces.put("b8", new Knight(BLACK));
        pieces.put("g8", new Knight(BLACK));
    }

    private void addBishops(Map<String, Piece> pieces) {
        pieces.put("c1", new Bishop(WHITE));
        pieces.put("f1", new Bishop(WHITE));
        pieces.put("c8", new Bishop(BLACK));
        pieces.put("f8", new Bishop(BLACK));
    }

    private void addRoyals(Map<String, Piece> pieces) {
        pieces.put("e1", new King(WHITE));
        pieces.put("e8", new King(BLACK));
        pieces.put("d1", new Queen(WHITE));
        pieces.put("d8", new Queen(BLACK));
    }

    private void validateMove(String move) {
        if (!moveValidator.isValid(move)) {
            throw new IllegalArgumentException("Invalid move: " + move);
        }
    }

    private void handleMove(String move) {
        String fromSquare = extractFromSquare(move);
        String toSquare = extractToSquare(move);
        Piece piece = pieces.get(fromSquare);
        if (piece instanceof Pawn) {
            handlePawnMove(fromSquare, toSquare);
        } else if (piece instanceof King && isCastlingMove(fromSquare, toSquare)) {
            handleCastling(fromSquare, toSquare);
        } else {
            movePiece(fromSquare, toSquare);
        }
    }

    private void updateBoardState(String toSquare) {
        boolean capture = pieces.containsKey(toSquare);
        boardState.update(toSquare, capture);
    }

    private void resetHalfMoveClock() {
        boardState.setHalfMoveClock(0);
    }

    private void handlePawnMove(String fromSquare, String toSquare) {
        resetHalfMoveClock();
        movePiece(fromSquare, toSquare);
        if (isTwoStepVerticalMove(fromSquare, toSquare)) {
            handleEnPassant(toSquare);
        }
        if (isPromotionSquare(toSquare)) {
            promotePawn(toSquare);
        }
    }

    private boolean isTwoStepVerticalMove(String fromSquare, String toSquare) {
        return Math.abs(fromSquare.charAt(1) - toSquare.charAt(1)) == 2;
    }

    private void handleEnPassant(String toSquare) {
        boardState.handleEnPassantTarget(toSquare);
    }

    private boolean isPromotionSquare(String position) {
        String rank = position.substring(1);
        return "1".equals(rank) || "8".equals(rank);
    }

    private void promotePawn(String position) {
        Piece pawn = pieces.remove(position);
        Piece newPiece = new Queen(pawn.getColor());
        pieces.put(position, newPiece);
    }

    private void movePiece(String fromSquare, String toSquare) {
        Piece piece = pieces.remove(fromSquare);
        pieces.put(toSquare, piece);
    }

    private String extractFromSquare(String move) {
        return move.substring(0, 2);
    }

    private String extractToSquare(String move) {
        return move.substring(2, 4);
    }

    private boolean isCastlingMove(String fromSquare, String toSquare) {
        return isTwoStepHorizontalMove(fromSquare, toSquare);
    }

    private boolean isTwoStepHorizontalMove(String fromSquare, String toSquare) {
        return Math.abs(fromSquare.charAt(0) - toSquare.charAt(0)) == 2;
    }

    private void handleCastling(String fromSquare, String toSquare) {
        String rookFromSquare = determineRookOriginalSquare(toSquare);
        String rookToSquare = determineRookDestinationSquare(toSquare);
        movePiece(fromSquare, toSquare);
        movePiece(rookFromSquare, rookToSquare);
    }

    private String determineRookOriginalSquare(String toSquare) {
        return (toSquare.charAt(0) == 'g' ? "h" : "a") + toSquare.charAt(1);
    }

    private String determineRookDestinationSquare(String toSquare) {
        return (toSquare.charAt(0) == 'g' ? "f" : "d") + toSquare.charAt(1);
    }
}
