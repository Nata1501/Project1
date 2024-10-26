package by.project.service.entertainment;


import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EntertainmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntertainmentServiceApplication.class, args);
	}


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
