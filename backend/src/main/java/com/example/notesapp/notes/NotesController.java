package com.example.notesapp.notes;

import com.example.notesapp.common.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

/**
 * NotesController - handles CRUD for notes with ETag/If-Match optimistic locking.
 */
@RestController
@RequestMapping("/api/notes")
public class NotesController {
    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteUpdateDTO body, Principal principal) {
        if (body == null || body.title == null || body.content == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title and content are required");
        }
        Note note = notesService.createNote(principal.getName(), body.title, body.content);
        NoteDTO dto = toDTO(note);
        return ResponseEntity
                .created(URI.create("/api/notes/" + note.getId()))
                .body(dto);
    }

    @GetMapping
    public ApiResponse listNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Principal principal) {

        // notesPage is Page<Note>
        Page<Note> notesPage = notesService.listNotes(principal.getName(), page, size);

        // Convert to Page<NoteDTO>
        Page<NoteDTO> dtos = notesPage.map(this::toDTO);

        // Return ApiResponse with dto content and pagination info from dtos
        return new ApiResponse(
                dtos.getContent(),
                dtos.getNumber(),
                dtos.getSize(),
                dtos.getTotalElements()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNote(@PathVariable UUID id, Principal principal) {
        Note note = notesService.getNote(principal.getName(), id);
        NoteDTO dto = toDTO(note);
        // Return weak ETag with version: W/"<version>"
        String etag = "W/\"" + note.getVersion() + "\"";
        return ResponseEntity.ok()
                .eTag(etag)
                .body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(
            @PathVariable UUID id,
            @RequestHeader(name = "If-Match", required = false) String ifMatch,
            @RequestBody NoteUpdateDTO body,
            Principal principal) {

        if (body == null || body.title == null || body.content == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title and content are required");
        }
        int version = parseIfMatchVersion(ifMatch);
        Note note = notesService.updateNote(principal.getName(), id, body.title, body.content, version);
        NoteDTO dto = toDTO(note);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id, Principal principal) {
        notesService.deleteNote(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }

    // --- helpers ---

    /**
     * Accepts If-Match as either W/"<version>" or "<version>".
     * Throws 400 if missing/invalid, 409 should be thrown by service on mismatch.
     */
    private int parseIfMatchVersion(String ifMatch) {
        if (ifMatch == null || ifMatch.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing If-Match header");
        }
        String v = ifMatch.trim();
        // Strip weak validator prefix if present
        if (v.regionMatches(true, 0, "W/", 0, 2)) {
            v = v.substring(2).trim();
        }
        // Strip surrounding quotes
        if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) {
            v = v.substring(1, v.length() - 1);
        }
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException nfe) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid If-Match header");
        }
    }

    private NoteDTO toDTO(Note note) {
        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setShared(note.isShared());
        dto.setVersion(note.getVersion());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setUpdatedAt(note.getUpdatedAt());
        return dto;
    }

}
