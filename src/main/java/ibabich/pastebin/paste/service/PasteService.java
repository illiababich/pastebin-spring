package ibabich.pastebin.paste.service;

import ibabich.pastebin.hashgenerator.HashService;
import ibabich.pastebin.paste.model.Paste;
import ibabich.pastebin.paste.repository.PasteRepository;
import ibabich.pastebin.user.User;
import ibabich.pastebin.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PasteService {
    private final PasteRepository pasteRepository;
    private final HashService hashService;
    private final UserRepository userRepository;

    @Transactional
    public Paste createPaste(Paste request) {
        Paste newPaste = Paste.builder()
                .content(request.getContent())
                .expiresAt(request.getExpiresAt())
                .createdAt(LocalDateTime.now())
                .accessLevel(request.getAccessLevel())
//                .userId(request.getUserId()) // TODO: add user assignment
                .pasteTitle(request.getPasteTitle())
                .password(request.getPassword())
                .build();

        newPaste = pasteRepository.save(newPaste);
        hashService.assignHashToPaste(newPaste);

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId().getId())
                    .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
            newPaste.setUserId(user);
            pasteRepository.save(newPaste);
        }

        return pasteRepository.save(newPaste);
    }

    public Optional<Paste> getPasteById(String pasteId) {
        return Optional.ofNullable(pasteRepository.findById(pasteId)).orElseThrow(
                () -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
    }
}
