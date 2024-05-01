package grim.readmechess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ReadmeChessApplication {

	// TODO: If game is over start a new one.
	// TODO: Add counter for number of games played. Won and lost.
	// TODO: Add game history and replay feature for current game.
	// TODO: Add exception handling for user inputs and logging for internal errors.
	public static void main(String[] args) {
		SpringApplication.run(ReadmeChessApplication.class, args);
	}
}
