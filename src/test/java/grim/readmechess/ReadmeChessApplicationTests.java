package grim.readmechess;

import grim.readmechess.model.chesspieces.Bishop;
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
}
