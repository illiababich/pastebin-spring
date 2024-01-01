package ibabich.pastebin.hashgenerator.service.scheduler;

import ibabich.pastebin.hashgenerator.model.Hash;
import ibabich.pastebin.hashgenerator.repository.HashRepository;
import ibabich.pastebin.hashgenerator.service.HashGenerator;
import ibabich.pastebin.logging.repository.LogEntryRepository;
import ibabich.pastebin.paste.repository.PasteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@AllArgsConstructor
public class HashScheduler {
    private final HashRepository hashRepository;
    private final PasteRepository pasteRepository;
    private final LogEntryRepository logEntryRepository;

    /**
     * <p>Method to generate a specified number of hashes</p>
     */
    @Scheduled(fixedDelay = 5000)
    public void generateHashes() {
        long existingHashes = hashRepository.countByPasteIsNull();
        int hashesToGenerate = 10 - (int)existingHashes;

        if (hashesToGenerate > 0) {
            List<Hash> newHashes = IntStream.range(0, hashesToGenerate).mapToObj(i -> {
                try {
                    HashGenerator generator = new HashGenerator(logEntryRepository);
                    return generator.generateUniqueHash(pasteRepository);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());

            hashRepository.saveAll(newHashes);
        }
    }
}
