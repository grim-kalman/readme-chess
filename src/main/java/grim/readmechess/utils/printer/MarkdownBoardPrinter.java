package grim.readmechess.utils.printer;

import grim.readmechess.model.chessboard.Board;
import grim.readmechess.model.chesspieces.Pawn;
import grim.readmechess.model.chesspieces.Piece;
import grim.readmechess.utils.validator.MoveValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MarkdownBoardPrinter extends BoardPrinter {

    private final MoveValidator moveValidator;

    @Autowired
    public MarkdownBoardPrinter(Board board, MoveValidator moveValidator) {
        super(board);
        this.moveValidator = moveValidator;
    }

    @Override
    public String print() {
        String introduction = """
        # Readme Chess
        
        Welcome to my GitHub profile! Here, you can play a game of chess with me, using my [readme-chess](https://github.com/grim-kalman/readme-chess) application.
        
        ## How to Play
        
        - **Select a piece:** Click on any selectable piece ([**A**]()) to select it.
        - **Move the piece:** Click on any destination square ([**_**]()) to move the selected piece.
        - **Wait for the page to refresh:** After each action, please wait for the page to refresh to see the updated game state.
        
        ## Chess Board
        """;
        String boardMarkdown = convertBoardToMarkdown();
        return introduction + boardMarkdown;
    }

    private String convertBoardToMarkdown() {
        String header = "|     |  a  |  b  |  c  |  d  |  e  |  f  |  g  |  h  |\n";
        String separator = "|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|\n";
        String boardRepresentation = formatBoardRepresentation(createBoardRepresentation()) + "\n";
        String newGameButton = "[![New Game](https://img.shields.io/badge/new_game-4CAF50)](https://readmechess.azurewebsites.net/new)";
        return header + separator + boardRepresentation + newGameButton;
    }

    private String formatBoardRepresentation(String[][] boardRepresentation) {
        return IntStream.range(0, boardRepresentation.length)
                .mapToObj(i -> formatMarkdownRow(boardRepresentation[i], boardRepresentation.length - i))
                .collect(Collectors.joining("\n")) + "\n";
    }

    private String formatMarkdownRow(String[] row, int rowNumber) {
        return "|  **" + rowNumber + "**  |  " + convertRowToMarkdown(row, rowNumber) + "  |";
    }

    private String convertRowToMarkdown(String[] row, int rowNumber) {
        return IntStream.range(0, row.length)
                .mapToObj(i -> convertSquareToMarkdown(row[i], (char) ('a' + i) + Integer.toString(rowNumber)))
                .collect(Collectors.joining("  |  "));
    }

    private String convertSquareToMarkdown(String square, String position) {
        String selectedSquare = board.getSelectedSquare();
        Piece selectedPiece = board.getPieceService().getPieces().get(selectedSquare);
        String squareSymbol = formatSquareSymbol(square);

        String move = formatMove(selectedPiece, selectedSquare + position, position);
        if (moveValidator.isValid(move)) {
            return createMarkdownLink(squareSymbol, "https://readmechess.azurewebsites.net/play?move=" + move);
        }

        if (moveValidator.isStartOfValidMove(position)) {
            return createMarkdownLink(squareSymbol, "https://readmechess.azurewebsites.net/select?square=" + position);
        }
        if (square == null) {
            return " ";
        }
        if (Character.isUpperCase(square.charAt(0))) {
            return createMarkdownLink(squareSymbol, "https://github.com/grim-kalman");
        }
        return squareSymbol;
    }

    String formatMove(Piece piece, String move, String position) {
        if (piece instanceof Pawn && position.endsWith("8")) {
            return move + "q";
        }
        return move;
    }

    private String formatSquareSymbol(String square) {
        if (square == null) {
            return "_";
        }
        if (Character.isLowerCase(square.charAt(0))) {
            return "_" + square + "_";
        }
        return "**" + square + "**";
    }

    private String createMarkdownLink(String text, String url) {
        return "[" + text + "](" + url + ")";
    }

}
