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
    public static final int SQUARE_SIZE = 40;

    private final BoardState boardState;
    private final List<Piece> pieces;
    private final MoveValidator moveValidator;

    public Board(BoardState boardState, MoveValidator moveValidator) {
        this.boardState = boardState;
        this.pieces = setupStartingPosition();
        this.moveValidator = moveValidator;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    List<Piece> setupStartingPosition() {
        List<Piece> startingPieces = new ArrayList<>();
        addKings(startingPieces);
        addQueens(startingPieces);
        addRooks(startingPieces);
        addKnights(startingPieces);
        addBishops(startingPieces);
        addPawns(startingPieces);
        return startingPieces;
    }

    private void addKings(List<Piece> pieces) {
        pieces.add(new King(WHITE, "e1"));
        pieces.add(new King(BLACK, "e8"));
    }

    private void addQueens(List<Piece> pieces) {
        pieces.add(new Queen(WHITE, "d1"));
        pieces.add(new Queen(BLACK, "d8"));
    }

    private void addRooks(List<Piece> pieces) {
        pieces.add(new Rook(WHITE, "a1"));
        pieces.add(new Rook(WHITE, "h1"));
        pieces.add(new Rook(BLACK, "a8"));
        pieces.add(new Rook(BLACK, "h8"));
    }

    private void addKnights(List<Piece> pieces) {
        pieces.add(new Knight(WHITE, "b1"));
        pieces.add(new Knight(WHITE, "g1"));
        pieces.add(new Knight(BLACK, "b8"));
        pieces.add(new Knight(BLACK, "g8"));
    }

    private void addBishops(List<Piece> pieces) {
        pieces.add(new Bishop(WHITE, "c1"));
        pieces.add(new Bishop(WHITE, "f1"));
        pieces.add(new Bishop(BLACK, "c8"));
        pieces.add(new Bishop(BLACK, "f8"));
    }

    private void addPawns(List<Piece> pieces) {
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
        pieces.stream()
                .filter(piece -> piece.getPosition().equals(fromSquare))
                .findFirst()
                .ifPresent(piece -> {
                    piece.setPosition(toSquare);
                    if (piece instanceof Pawn) {
                        boardState.setHalfMoveClock(0);
                        boardState.handleEnPassantTarget(fromSquare, toSquare);
                    }
                });
    }
}
