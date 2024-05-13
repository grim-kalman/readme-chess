package grim.readmechess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReadmeChessApplication {

	// TODO: If game is over start a new one.
	// TODO: Add exception handling for user inputs and logging for internal errors.
	// TODO: Add counter for number of games played. Won and lost.
	public static void main(String[] args) {
		SpringApplication.run(ReadmeChessApplication.class, args);
	}
}
