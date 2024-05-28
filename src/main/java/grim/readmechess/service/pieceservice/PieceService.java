package grim.readmechess.service.pieceservice;

import grim.readmechess.model.chesspieces.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static grim.readmechess.utils.common.Constants.BLACK;
import static grim.readmechess.utils.common.Constants.WHITE;

@Service
@Getter
public class PieceService {

    private Map<String, Piece> pieces;

    public void reset() {
        this.pieces = setupPieces();
    }

    public Map<String, Piece> setupPieces() {
        Map<String, Piece> startingPieces = new HashMap<>();
        placePawns(startingPieces);
        placeRooks(startingPieces);
        placeKnights(startingPieces);
        placeBishops(startingPieces);
        placeRoyals(startingPieces);
        return startingPieces;
    }

    private void placePawns(Map<String, Piece> pieces) {
        for (char col = 'a'; col <= 'h'; col++) {
            pieces.put("" + col + '2', new Pawn(WHITE));
            pieces.put("" + col + '7', new Pawn(BLACK));
        }
    }

    private void placeRooks(Map<String, Piece> pieces) {
        pieces.put("a1", new Rook(WHITE));
        pieces.put("h1", new Rook(WHITE));
        pieces.put("a8", new Rook(BLACK));
        pieces.put("h8", new Rook(BLACK));
    }

    private void placeKnights(Map<String, Piece> pieces) {
        pieces.put("b1", new Knight(WHITE));
        pieces.put("g1", new Knight(WHITE));
        pieces.put("b8", new Knight(BLACK));
        pieces.put("g8", new Knight(BLACK));
    }

    private void placeBishops(Map<String, Piece> pieces) {
        pieces.put("c1", new Bishop(WHITE));
        pieces.put("f1", new Bishop(WHITE));
        pieces.put("c8", new Bishop(BLACK));
        pieces.put("f8", new Bishop(BLACK));
    }

    private void placeRoyals(Map<String, Piece> pieces) {
        pieces.put("e1", new King(WHITE));
        pieces.put("d1", new Queen(WHITE));
        pieces.put("e8", new King(BLACK));
        pieces.put("d8", new Queen(BLACK));
    }
}
