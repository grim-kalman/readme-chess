package grim.readmechess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReadmeChessApplication {

	// TODO: If game is over start a new one. Have a checkmate and stalemate condition.
	// TODO: Add exception handling for user inputs and logging for internal errors.
	// TODO: Add a redirect to the github page if someone hits the wrong endpoint or faulty input.
	// TODO: Add counter for number of games played. Won and lost.
	// TODO: Add security to the game.
	// TODO: Add banter to the game.
	public static void main(String[] args) {
		SpringApplication.run(ReadmeChessApplication.class, args);
	}
}
