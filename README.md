# Readme Chess

Readme Chess is a Java-based web application built with Spring Boot and Maven. The application allows users to play a game of chess, with the game state being updated and displayed in the profile README on GitHub.

## How It Works

1. When a user makes a move or selects a square on the chess board, the corresponding endpoint in the Controller.java is triggered.
2. The ChessService processes the move, updates the game state, and recalculates the best move for the computer player using the Stockfish chess engine.
3. The updated game state is then pushed to the my GitHub profile README file using the GitHub API.
4. Lastly, the user is redirected back to the GitHub profile page to see the updated chess board.

## Future Improvements
1. Add banter where the computer player makes comments during the game.
2. Train a NNUE model based on my own games to let people play against something resembling my playstyle.