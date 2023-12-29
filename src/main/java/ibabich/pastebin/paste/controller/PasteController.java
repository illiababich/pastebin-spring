package ibabich.pastebin.paste.controller;

import ibabich.pastebin.paste.model.Paste;
import ibabich.pastebin.paste.service.PasteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paste")
@AllArgsConstructor
public class PasteController {
    private final PasteService pasteService;

    @PostMapping(consumes = "application/json")
    public Paste createPaste(@RequestBody Paste paste) {
        return pasteService.createPaste(paste);
    }

    @GetMapping(path = "/{pasteId}") // TODO: add popularity scale. Create a separate table for that
    public ResponseEntity<Paste> getPasteById(@PathVariable String pasteId) {
        return ResponseEntity.of(pasteService.getPasteById(pasteId));
    }
}
