package grim.readmechess.webapi.chessboard;

import grim.readmechess.webapi.chesspieces.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BoardPrinterTest {
    private Board board;
    private BoardPrinter boardPrinter;

    @BeforeEach
    public void setup() {
        board = Mockito.mock(Board.class);
        boardPrinter = new BoardPrinter(board);
    }

    @Test
    void printFENReturnsCorrectFENString() {
        Piece piece = Mockito.mock(Piece.class);
        when(piece.getPosition()).thenReturn("e1");
        when(piece.getFenSymbol()).thenReturn("K");
        when(board.getPieces()).thenReturn(List.of(piece));
        when(board.getActiveColor()).thenReturn("w");
        when(board.getCastlingAvailability()).thenReturn("KQkq");
        when(board.getEnPassantTarget()).thenReturn("-");
        when(board.getHalfMoveClock()).thenReturn(0);
        when(board.getFullMoveNumber()).thenReturn(1);

        String expectedFEN = "8/8/8/8/8/8/8/4K3 w KQkq - 0 1";
        String actualFEN = boardPrinter.printFEN();

        assertEquals(expectedFEN, actualFEN);
    }

    @Test
    void printFENHandlesEmptyBoard() {
        when(board.getPieces()).thenReturn(List.of());
        when(board.getActiveColor()).thenReturn("w");
        when(board.getCastlingAvailability()).thenReturn("KQkq");
        when(board.getEnPassantTarget()).thenReturn("-");
        when(board.getHalfMoveClock()).thenReturn(0);
        when(board.getFullMoveNumber()).thenReturn(1);

        String expectedFEN = "8/8/8/8/8/8/8/8 w KQkq - 0 1";
        String actualFEN = boardPrinter.printFEN();

        assertEquals(expectedFEN, actualFEN);
    }
}