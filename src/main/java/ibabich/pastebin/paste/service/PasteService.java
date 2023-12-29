package ibabich.pastebin.paste.service;

import ibabich.pastebin.hashgenerator.HashService;
import ibabich.pastebin.paste.model.Paste;
import ibabich.pastebin.paste.model.PasteDto;
import ibabich.pastebin.paste.repository.PasteRepository;
import ibabich.pastebin.creator.model.Creator;
import ibabich.pastebin.creator.repository.CreatorRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                .expiresAt(request.getExpiresAt())
                .createdAt(LocalDateTime.now())
                .accessLevel(request.getAccessLevel())
                .creator(request.getCreatorId() != null
                        ? creatorRepository.findById(request.getCreatorId()).orElse(null)
                        : null)
                .pasteTitle(request.getPasteTitle())
                .password(request.getPassword())
                .build();

        newPaste = pasteRepository.save(newPaste);
        hashService.assignHashToPaste(newPaste);

        return pasteRepository.save(newPaste);
    }

    public Optional<Paste> getPasteByHash(String hash) {
        return Optional.ofNullable(pasteRepository.findByHash(hash)).orElseThrow(
                () -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
    }

    public List<Paste> getPastesByUserId(Long userId) {
        return pasteRepository.findByCreator_Id(userId);
    }
}
