package ibabich.pastebin.hashgenerator.service;

import ibabich.pastebin.hashgenerator.model.Hash;
import ibabich.pastebin.hashgenerator.repository.HashRepository;
import ibabich.pastebin.logging.service.DatabaseLogger;
import ibabich.pastebin.logging.model.ErrorLevel;
import ibabich.pastebin.logging.repository.LogEntryRepository;
import ibabich.pastebin.paste.model.Paste;
import ibabich.pastebin.paste.repository.PasteRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class HashService {
    private final HashRepository hashRepository;
    private final PasteRepository pasteRepository;
    private final LogEntryRepository logEntryRepository;
    private static final int MAX_RETRIES = 10;

    // Method to generate a specified number of hashes
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

    // Method to assign a hash to a paste
    @Transactional
    public void assignHashToPaste(Paste paste) {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            Hash hash = hashRepository.findFirstByPasteIsNull();
            if (hash != null) {
                hash.setPaste(paste);
                hashRepository.save(hash);

                paste.setHash(hash);
                pasteRepository.save(paste);
                break;
            } else {
                try {
                    Thread.sleep(1000);
                    DatabaseLogger databaseLogger = new DatabaseLogger();
                    databaseLogger.createLog(
                            ErrorLevel.HASH_TO_PASTE_ASSIGNMENT,
                            "An error occurred while retrieving the pre-generated hash.",
                            logEntryRepository);
                    assignHashToPaste(paste);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                retries++;
            }
        }
    }
}
