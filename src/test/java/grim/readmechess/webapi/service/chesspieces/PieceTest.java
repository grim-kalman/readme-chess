package grim.readmechess.webapi.service.chesspieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static grim.readmechess.utils.Constants.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PieceTest {
    private Piece piece;

    @BeforeEach
    public void setup() {
        piece = new Piece(WHITE, "K", "k", "e1") {
            @Override
            public String toSvgString() {
                return super.toSvgString();
            }
        };
    }

    @Test
    void fenSymbolIsReturnedCorrectly() {
        assertEquals("k", piece.getFenSymbol());
    }

    @Test
    void positionIsReturnedCorrectly() {
        assertEquals("e1", piece.getPosition());
    }

    @Test
    void positionIsSetCorrectly() {
        piece.setPosition("e2");
        assertEquals("e2", piece.getPosition());
    }

    @Test
    void svgStringIsReturnedCorrectlyForWhitePiece() {
        String expectedSvg = "<text x=\"220\" y=\"346\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">K</text>";
        assertEquals(expectedSvg, piece.toSvgString());
    }

    @Test
    void svgStringIsReturnedCorrectlyForBlackPiece() {
        piece = new Piece("b", "K", "k", "e1") {
            @Override
            public String toSvgString() {
                return super.toSvgString();
            }
        };
        String expectedSvg = "<text x=\"220\" y=\"346\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">K</text>";
        assertEquals(expectedSvg, piece.toSvgString());
    }
}
