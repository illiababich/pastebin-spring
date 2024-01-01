package ibabich.pastebin.hashgenerator.service;

import ibabich.pastebin.hashgenerator.model.Hash;
import ibabich.pastebin.hashgenerator.repository.HashRepository;
import ibabich.pastebin.logging.service.DatabaseLogger;
import ibabich.pastebin.logging.model.ErrorLevel;
import ibabich.pastebin.logging.repository.LogEntryRepository;
import ibabich.pastebin.paste.model.Paste;
import ibabich.pastebin.paste.repository.PasteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class HashService {
    private final HashRepository hashRepository;
    private final PasteRepository pasteRepository;
    private final LogEntryRepository logEntryRepository;
    private static final int MAX_RETRIES = 10;

    /**
     * <p>Method to assign a hash to a paste</p>
     * @param paste a Paste object that gets assigned a hash
     */
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
