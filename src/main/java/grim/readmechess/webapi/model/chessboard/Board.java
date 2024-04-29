package grim.readmechess.webapi.model.chessboard;

import grim.readmechess.webapi.model.chesspieces.*;
import grim.readmechess.webapi.validator.MoveValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static grim.readmechess.utils.Constants.BLACK;
import static grim.readmechess.utils.Constants.WHITE;

@Component
public class Board {

    private final BoardState boardState;
    private final List<Piece> pieces;
    private final MoveValidator moveValidator;
    private String selectedSquare;

    public Board(BoardState boardState, MoveValidator moveValidator) {
        this.boardState = boardState;
        this.pieces = setupStartingPosition();
        this.moveValidator = moveValidator;
    }

    public void selectSquare(String square) {
        this.selectedSquare = square.equals(this.selectedSquare) ? null : square;
    }

    public String getSelectedSquare() {
        return selectedSquare;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    List<Piece> setupStartingPosition() {
        List<Piece> startingPieces = new ArrayList<>();
        addPieces(startingPieces);
        return startingPieces;
    }

    private void addPieces(List<Piece> pieces) {
        pieces.add(new King(WHITE, "e1"));
        pieces.add(new King(BLACK, "e8"));
        pieces.add(new Queen(WHITE, "d1"));
        pieces.add(new Queen(BLACK, "d8"));
        pieces.add(new Rook(WHITE, "a1"));
        pieces.add(new Rook(WHITE, "h1"));
        pieces.add(new Rook(BLACK, "a8"));
        pieces.add(new Rook(BLACK, "h8"));
        pieces.add(new Knight(WHITE, "b1"));
        pieces.add(new Knight(WHITE, "g1"));
        pieces.add(new Knight(BLACK, "b8"));
        pieces.add(new Knight(BLACK, "g8"));
        pieces.add(new Bishop(WHITE, "c1"));
        pieces.add(new Bishop(WHITE, "f1"));
        pieces.add(new Bishop(BLACK, "c8"));
        pieces.add(new Bishop(BLACK, "f8"));
        for (char col = 'a'; col <= 'h'; col++) {
            pieces.add(new Pawn(WHITE, "" + col + '2'));
            pieces.add(new Pawn(BLACK, "" + col + '7'));
        }
    }

    public void makeMove(String move) {
        if (moveValidator.isValid(move)) {
            String fromSquare = move.substring(0, 2);
            String toSquare = move.substring(2, 4);
            updateBoardState(fromSquare, toSquare);
            movePiece(fromSquare, toSquare);
        } else {
            throw new IllegalArgumentException("Invalid move: " + move);
        }
    }

    private void updateBoardState(String fromSquare, String toSquare) {
        boardState.updateActiveColor();
        boardState.resetEnPassantTarget();
        boardState.updateCastlingRights(fromSquare);
        boardState.updateHalfMoveClock(toSquare, pieces);
    }

    private void movePiece(String fromSquare, String toSquare) {
        for (Piece piece : pieces) {
            if (piece.getPosition().equals(fromSquare)) {
                piece.setPosition(toSquare);
                if (piece instanceof Pawn) {
                    boardState.setHalfMoveClock(0);
                    boardState.handleEnPassantTarget(fromSquare, toSquare);
                } else if (isCastlingMove(piece, fromSquare, toSquare)) {
                    handleCastling(toSquare);
                }
                break;
            }
        }
    }

    private boolean isCastlingMove(Piece piece, String fromSquare, String toSquare) {
        return piece instanceof King && Math.abs(fromSquare.charAt(0) - toSquare.charAt(0)) == 2;
    }

    private void handleCastling(String toSquare) {
        String rookFromSquare = (toSquare.charAt(0) == 'g' ? "h" : "a") + toSquare.charAt(1);
        String rookToSquare = (toSquare.charAt(0) == 'g' ? "f" : "d") + toSquare.charAt(1);
        movePiece(rookFromSquare, rookToSquare);
    }
}
