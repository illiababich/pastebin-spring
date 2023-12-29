package ibabich.pastebin.scheduler;

import ibabich.pastebin.hashgenerator.HashRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class SchedulerTest {
    private final HashRepository hashRepository;

    @Scheduled(fixedDelay = 5000)
    public void printHashWithNoPasteCount() {
        final long hashWithNoPasteCount = hashRepository.countByPasteIsNull();
        log.info("There are {} hashes with no Paste in the database", hashWithNoPasteCount);
    }
}
