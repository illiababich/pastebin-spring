package ibabich.pastebin.logging.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean(name = "asyncLogWriter")
    public Executor asyncLogWriter() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        threadPoolTaskExecutor.setCorePoolSize(3);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setMaxPoolSize(6);
        threadPoolTaskExecutor.setThreadNamePrefix("async-log-writer-");

        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
