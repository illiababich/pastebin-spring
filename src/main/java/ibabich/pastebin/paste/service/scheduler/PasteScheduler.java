package ibabich.pastebin.paste.service.scheduler;

import ibabich.pastebin.paste.model.Paste;
import ibabich.pastebin.paste.repository.PasteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@Slf4j
@AllArgsConstructor
public class PasteScheduler {
    private final PasteRepository pasteRepository;

    @Scheduled(fixedRate = 10000)
    public void disableExpiredPastes() {
        List<Paste> pastes = pasteRepository.findByEnabledTrue();
        pastes.forEach(paste -> {
            if (isPasteExpired(paste)) {
                disablePaste(paste);
            }
        });
    }

    private boolean isPasteExpired(Paste paste) {
        String expiresAt = paste.getExpiresAt();
        if (expiresAt == null || expiresAt.equalsIgnoreCase("never")) {
            return false;
        }

        Date now = new Date();
        Date expirationDate = parseStringToDate(expiresAt);
        assert expirationDate != null;
        return expirationDate.before(now);
    }

    private void disablePaste(Paste paste) {
        paste.setEnabled(false);
        pasteRepository.save(paste);
    }

    private Date parseStringToDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
}
