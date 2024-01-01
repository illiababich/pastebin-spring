package ibabich.pastebin.paste.service;

import ibabich.pastebin.hashgenerator.service.HashService;
import ibabich.pastebin.paste.model.ExpirationOptions;
import ibabich.pastebin.paste.model.Paste;
import ibabich.pastebin.paste.model.PasteDto;
import ibabich.pastebin.paste.repository.PasteRepository;
import ibabich.pastebin.creator.repository.CreatorRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class PasteService {
    private final PasteRepository pasteRepository;
    private final HashService hashService;
    private final CreatorRepository creatorRepository;

    @Transactional
    public Paste createPaste(PasteDto request) {
        Paste newPaste = Paste.builder()
                .content(request.getContent())
                .accessLevel(request.getAccessLevel())
                .creator(request.getCreatorId() != null
                        ? creatorRepository.findById(request.getCreatorId()).orElse(null)
                        : null)
                .pasteTitle(request.getPasteTitle())
                .password(request.getPassword())
                .enabled(true)
                .build();

        Date date = new Date();
        newPaste.setCreatedAtUtc(formatDateToString(date));

        String expiresAt = calculateExpirationTime(date, request.getExpiresAt());
        newPaste.setExpiresAt(expiresAt);

        newPaste = pasteRepository.save(newPaste);
        hashService.assignHashToPaste(newPaste);

        return pasteRepository.save(newPaste);
    }

    private String calculateExpirationTime(Date createdAt, ExpirationOptions option) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdAt);

        switch (option) {
            case MONTH -> calendar.add(Calendar.MONTH, 1);
            case WEEK -> calendar.add(Calendar.WEEK_OF_YEAR, 1);
            case THREE_DAYS -> calendar.add(Calendar.DAY_OF_MONTH, 3);
            case ONE_DAY -> calendar.add(Calendar.DAY_OF_MONTH, 1);
            case ONE_HOUR -> calendar.add(Calendar.HOUR_OF_DAY, 1);
            case ONE_MINUTE -> calendar.add(Calendar.MINUTE, 1);
        }

        return formatDateToString(calendar.getTime());
    }

    public Optional<Paste> getPasteByHash(String hash) {
        return Optional.ofNullable(pasteRepository.findByHash(hash)).orElseThrow(
                () -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
    }

    public List<Paste> getPastesByCreatorId(Long creatorId) {
        return pasteRepository.findByCreator_Id(creatorId);
    }

    private String formatDateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }
}
