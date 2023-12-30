package ibabich.pastebin.paste.controller;

import ibabich.pastebin.paste.model.Paste;
import ibabich.pastebin.paste.model.PasteDto;
import ibabich.pastebin.paste.service.PasteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paste")
@AllArgsConstructor
public class PasteController {
    private final PasteService pasteService;

    @PostMapping(consumes = "application/json")
    public Paste createPaste(@RequestBody PasteDto paste) {
        return pasteService.createPaste(paste);
    }

    @GetMapping(path = "/{hash}") // TODO: add popularity scale. Create a separate table for that
    public ResponseEntity<Paste> getPasteByHash(@PathVariable String hash) {
        return ResponseEntity.of(pasteService.getPasteByHash(hash));
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<Paste>> getPastesByCreatorId(@PathVariable Long creatorId) {
        List<Paste> pastes = pasteService.getPastesByCreatorId(creatorId);
        return ResponseEntity.ok(pastes);
    }
}
