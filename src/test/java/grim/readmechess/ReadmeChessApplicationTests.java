package grim.readmechess;

import grim.readmechess.webapi.chessboard.ChessBoard;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ReadmeChessApplicationTests {

	@Test
	void shouldPrintTheChessBoard() {
		// Arrange
		ChessBoard chessBoard = new ChessBoard("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");
		String expectedOutput =
						"r n b q k b n r\n" +
						"p p . p p p p p\n" +
						". . . . . . . .\n" +
						". . p . . . . .\n" +
						". . . . P . . .\n" +
						". . . . . N . .\n" +
						"P P P P . P P P\n" +
						"R N B Q K B . R\n";

		ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStreamCaptor));
		PrintStream originalOut = System.out;
		// Act
		chessBoard.printChessBoardToConsole();
		// Assert
		assertEquals(expectedOutput.trim(), outputStreamCaptor.toString().trim());
		System.setOut(originalOut);
	}
}
