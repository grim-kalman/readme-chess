package grim.readmechess.webapi.chessboard;

import grim.readmechess.utils.Utils;
import grim.readmechess.webapi.chesspieces.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static grim.readmechess.utils.Utils.convertColumnToIndex;
import static grim.readmechess.utils.Utils.convertRowToIndex;

@Component
public class Board {
    public static final int SQUARE_SIZE = 40;
    private static final String LIGHT_COLOR = "#D8AF87";
    private static final String DARK_COLOR = "#B58863";
    private static final String SQUARE_TEMPLATE = "<rect x=\"%d\" y=\"%d\" width=\"40\" height=\"40\" fill=\"%s\"/>";
    private static final String EDGE_LABELS_TEMPLATE = "<text x=\"%d\" y=\"%d\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">%s</text>";

    private List<Piece> pieces;
    private String activeColor;
    private String castlingAvailability;
    private String enPassantTarget;
    private int halfMoveClock;
    private int fullMoveNumber;

    public Board() {
        this.pieces = setupStartingPosition();
        this.activeColor = "w";
        this.castlingAvailability = "KQkq";
        this.enPassantTarget = "-";
        this.halfMoveClock = 0;
        this.fullMoveNumber = 1;
    }

    private List<Piece> setupStartingPosition() {
        List<Piece> startingPieces = new ArrayList<>();
        startingPieces.add(new King("white", "e1"));
        startingPieces.add(new King("black", "e8"));
        startingPieces.add(new Rook("white", "a1"));
        startingPieces.add(new Rook("white", "h1"));
        startingPieces.add(new Rook("black", "a8"));
        startingPieces.add(new Rook("black", "h8"));
        startingPieces.add(new Queen("white", "d1"));
        startingPieces.add(new Queen("black", "d8"));
        startingPieces.add(new Knight("white", "b1"));
        startingPieces.add(new Knight("white", "g1"));
        startingPieces.add(new Knight("black", "b8"));
        startingPieces.add(new Knight("black", "g8"));
        startingPieces.add(new Bishop("white", "c1"));
        startingPieces.add(new Bishop("white", "f1"));
        startingPieces.add(new Bishop("black", "c8"));
        startingPieces.add(new Bishop("black", "f8"));
        for (char col = 'a'; col <= 'h'; col++) {
            startingPieces.add(new Pawn("white", "" + col + '2'));
            startingPieces.add(new Pawn("black", "" + col + '7'));
        }
        return startingPieces;
    }

    public String toFenString() {
        String[][] board = new String[8][8];
        pieces.forEach(piece -> placePieceOnBoard(board, piece));
        String boardFen = Arrays.stream(board)
                .map(this::processRowForFen)
                .collect(Collectors.joining("/"));
        return String.format("%s %s %s %s %d %d",
                boardFen, activeColor, castlingAvailability, enPassantTarget, halfMoveClock, fullMoveNumber);
    }

    private void placePieceOnBoard(String[][] board, Piece piece) {
        String position = piece.getPosition();
        int col = convertColumnToIndex(position.charAt(0));
        int row = convertRowToIndex(position.charAt(1));
        board[row][col] = piece.getFenSymbol();
    }

    private String processRowForFen(String[] row) {
        StringBuilder rowFen = new StringBuilder();
        int emptySquares = 0;
        for (String square : row) {
            if (square == null) {
                emptySquares++;
            } else {
                if (emptySquares > 0) {
                    rowFen.append(emptySquares);
                    emptySquares = 0;
                }
                rowFen.append(square);
            }
        }
        if (emptySquares > 0) {
            rowFen.append(emptySquares);
        }
        return rowFen.toString();
    }

    public String toSvgString() {
        return String.format("<svg viewBox=\"0 0 400 400\" xmlns=\"http://www.w3.org/2000/svg\">%s%s%s</svg>",
                printSquares(), printEdgeLabels(), printPieces());
    }

    private String printEdgeLabels() {
        StringBuilder stringBuilder = new StringBuilder(3340);
        for (char col = 'a'; col <= 'h'; col++) {
            int x = 60 + SQUARE_SIZE * (col - 'a');
            stringBuilder.append(String.format(EDGE_LABELS_TEMPLATE, x, 20, col));
            stringBuilder.append(String.format(EDGE_LABELS_TEMPLATE, x, 380, col));
        }
        for (int row = 1; row <= 8; row++) {
            int y = 380 - SQUARE_SIZE * (row);
            stringBuilder.append(String.format(EDGE_LABELS_TEMPLATE, 20, y, row));
            stringBuilder.append(String.format(EDGE_LABELS_TEMPLATE, 380, y, row));
        }
        return stringBuilder.toString();
    }

    private String printSquares() {
        StringBuilder stringBuilder = new StringBuilder(3872);
        for (int row = 1; row <= 8; row++) {
            int y = SQUARE_SIZE * row;
            for (int col = 1; col <= 8; col++) {
                int x = SQUARE_SIZE * col;
                String color = (row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR;
                stringBuilder.append(String.format(SQUARE_TEMPLATE, x, y, color));
            }
        }
        return stringBuilder.toString();
    }

    private String printPieces() {
        return pieces.stream()
                .map(Piece::toSvgString)
                .collect(Collectors.joining());
    }

    public void makeMove(String move) {
        updateActiveColor();
        resetEnPassantTarget();
        // TODO: 2023-11-06  updateCastlingRights(move);
        String fromSquare = move.substring(0, 2);
        String toSquare = move.substring(2, 4);
        updateHalfMoveClock(toSquare);
        movePiece(fromSquare, toSquare);
    }

    private void updateActiveColor() {
        if ("b".equals(activeColor)) {
            fullMoveNumber++;
            activeColor = "w";
        } else {
            activeColor = "b";
        }
    }

    private void resetEnPassantTarget() {
        enPassantTarget = "-";
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
            enPassantTarget = toSquare.charAt(0) + (activeColor.equals("b") ? "3" : "6");
        }
    }
}
