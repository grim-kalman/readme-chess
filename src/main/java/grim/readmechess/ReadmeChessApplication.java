package grim.readmechess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class ReadmeChessApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadmeChessApplication.class, args);
	}
}
