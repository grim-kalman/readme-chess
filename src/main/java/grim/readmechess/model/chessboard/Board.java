package grim.readmechess.model.chessboard;

import grim.readmechess.model.chesspieces.*;
import grim.readmechess.service.pieceservice.PieceService;
import grim.readmechess.utils.validator.MoveValidator;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class Board {

    private String selectedSquare;
    private final BoardState boardState;
    private final PieceService pieceService;
    private final MoveValidator moveValidator;


    @PostConstruct
    public void init() {
        reset();
    }

    public void reset() {
        this.selectedSquare = null;
        this.boardState.reset();
        this.pieceService.reset();
    }

    public void selectSquare(String square) {
        this.selectedSquare = square.equals(this.selectedSquare) ? null : square;
    }

    public void makeMove(String move) {
        validateMove(move);
        handleMove(move);
        updateBoardState(extractToSquare(move));
    }

    private void validateMove(String move) {
        if (!moveValidator.isValid(move)) {
            throw new IllegalArgumentException("Invalid move: " + move);
        }
    }

    private void handleMove(String move) {
        String fromSquare = extractFromSquare(move);
        String toSquare = extractToSquare(move);
        Piece piece = pieceService.getPieces().get(fromSquare);
        if (piece instanceof Pawn) {
            handlePawnMove(fromSquare, toSquare);
        } else if (isCastlingMove(piece, fromSquare, toSquare)) {
            handleCastling(fromSquare, toSquare);
        } else {
            movePiece(fromSquare, toSquare);
        }
    }

    private void updateBoardState(String toSquare) {
        boolean capture = pieceService.getPieces().containsKey(toSquare);
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
        Piece pawn = pieceService.getPieces().remove(position);
        Piece newPiece = new Queen(pawn.getColor());
        pieceService.getPieces().put(position, newPiece);
    }

    private void movePiece(String fromSquare, String toSquare) {
        Piece piece = pieceService.getPieces().remove(fromSquare);
        pieceService.getPieces().put(toSquare, piece);
    }

    private String extractFromSquare(String move) {
        return move.substring(0, 2);
    }

    private String extractToSquare(String move) {
        return move.substring(2, 4);
    }

    private boolean isCastlingMove(Piece piece, String fromSquare, String toSquare) {
        return piece instanceof King && isTwoStepHorizontalMove(fromSquare, toSquare);
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
