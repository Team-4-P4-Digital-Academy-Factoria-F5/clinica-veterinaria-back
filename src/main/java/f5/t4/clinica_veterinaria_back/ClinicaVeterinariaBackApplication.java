package f5.t4.clinica_veterinaria_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClinicaVeterinariaBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicaVeterinariaBackApplication.class, args);
	}

}
