package com.example.notesapp.share;

import com.example.notesapp.notes.NoteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
public class ShareController {
    private final ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    @PostMapping("/api/notes/{id}/share")
    public ResponseEntity<?> shareNote(@PathVariable String id, Principal principal) {
        String shareId = shareService.createShare(principal.getName(), UUID.fromString(id));
        String shareUrl = String.format("%s/shared/%s", System.getenv("APP_ORIGIN"), shareId);
        return ResponseEntity.ok(Map.of("shareId", shareId, "shareUrl", shareUrl));
    }

    @DeleteMapping("/api/notes/{id}/share")
    public ResponseEntity<Void> revokeShare(@PathVariable String id, Principal principal) {
        shareService.revokeShare(principal.getName(), UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/public/notes/{shareId}")
    public ResponseEntity<NoteDTO> getSharedNote(@PathVariable String shareId) {
        NoteDTO note = shareService.getPublicNote(shareId);
        return ResponseEntity.ok(note);
    }
}
