package grim.readmechess.webapi.service;

import grim.readmechess.webapi.chessboard.Board;

@org.springframework.stereotype.Service
public class Service {

    private final Board board;

    public Service(Board board) {
        this.board = board;
    }

    public void makeMove(String move) {

        //1. MakeMove
        board.makeMove(move);

        //2. Get best move from API or Stockfish and do it asynchronously?

        //3. MakeMove Update board state

        //4. Get all possible moves from the new position

        //5. Update the readme with all the new links and pieces

        //6. Make API call to GitHub to commit a new readme


    }


}
