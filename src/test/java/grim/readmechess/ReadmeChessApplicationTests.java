package grim.readmechess;

import grim.readmechess.webapi.chessboard.Board;
import grim.readmechess.webapi.chesspieces.Bishop;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ReadmeChessApplicationTests {
	@Test
	void testGetPosition() {
		// Arrange
		Bishop bishop = new Bishop("black", "c4");
		String expectedPosition = "c4";

		// Act
		String actualPosition = bishop.getPosition();

		// Assert
		assertEquals(expectedPosition, actualPosition);
	}

	@Test
	void testSetPosition() {
		// Arrange
		Bishop bishop = new Bishop("black", "c4");
		String newPosition = "d5";

		// Act
		bishop.setPosition(newPosition);
		String actualPosition = bishop.getPosition();

		// Assert
		assertEquals(newPosition, actualPosition);
	}

	@Test
	void testPieceToSvgString() {
		// Arrange
		Bishop bishop = new Bishop("black", "c4");
		String expectedSvg = "" +
				"<text x=\"140\" y=\"220\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"40\" text-anchor=\"middle\">♝</text>";

		// Act
		String actualSvg = bishop.toSvgString();

		// Assert
		assertEquals(expectedSvg, actualSvg);
	}

	@Test
	void testMakeMove() {
		// Arrange
		Board board = new Board();
		String move = "e2e4";

		// Act
		board.makeMove(move);

		// Act
		assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", board.toFenString());
	}

	@Test
	void testToSvgString() {
		// Arrange
		Board board = new Board();
		String move = "e2e4";

		// Act
		board.makeMove(move);

		// Act
		assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", board.toSvgString());
	}
}
