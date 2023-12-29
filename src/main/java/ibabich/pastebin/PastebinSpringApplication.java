package ibabich.pastebin;

import ibabich.pastebin.user.User;
import ibabich.pastebin.user.repository.UserRepository;
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
    public CommandLineRunner dataLoader (UserRepository userRepository) {
        return args -> {
            User user1 = new User("qwe", "rty");

            userRepository.save(user1);
        };
    }
}
