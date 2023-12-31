package ibabich.pastebin.logging.service;

import ibabich.pastebin.logging.model.ErrorLevel;
import ibabich.pastebin.logging.model.LogEntry;
import ibabich.pastebin.logging.repository.LogEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@AllArgsConstructor
@Service
public class DatabaseLogger {
    @Async("asyncLogWriter")
    public void createLog(ErrorLevel errorLevel, String message, LogEntryRepository logEntryRepository) {
        LogEntry logEntry = new LogEntry();
        logEntry.setErrorLevel(errorLevel);
        logEntry.setErrorMessage(message);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        logEntry.setDateTime(sdf.format(date));

        logEntryRepository.save(logEntry);
    }
}
