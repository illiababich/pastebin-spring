package ibabich.pastebin.logging;

import ibabich.pastebin.logging.model.ErrorLevel;
import ibabich.pastebin.logging.model.LogEntry;
import ibabich.pastebin.logging.repository.LogEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class DatabaseLogger {

    private final LogEntryRepository logEntryRepository;

    public void createLog(ErrorLevel errorLevel, String message) {
        LogEntry logEntry = new LogEntry();
        logEntry.setDateTime(String.valueOf(LocalDateTime.now()));
        logEntry.setErrorLevel(errorLevel);
        logEntry.setErrorMessage(message);

        logEntryRepository.save(logEntry);
    }
}
