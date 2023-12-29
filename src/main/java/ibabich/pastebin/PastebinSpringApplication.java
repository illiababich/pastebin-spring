package ibabich.pastebin;

import ibabich.pastebin.creator.model.Creator;
import ibabich.pastebin.creator.repository.CreatorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PastebinSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(PastebinSpringApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader (CreatorRepository creatorRepository) {
        return args -> {
            Creator creator1 = new Creator("username@gmail.com", "secure");

            creatorRepository.save(creator1);
        };
    }
}
