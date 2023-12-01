package hu.sceat.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan(basePackages = "hu.sceat.backend.presentation.shell")
public class SceatBackendApp {
	public static void main(String[] args) {
		SpringApplication.run(SceatBackendApp.class, args);
	}
}
