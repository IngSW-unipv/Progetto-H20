package carpool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
@EnableScheduling //Altrimenti i metodi scheduled che ci sono in giro non funzionano
public class SpringCarpoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCarpoolApplication.class, args);
	}
}

