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
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            Principal principal) {

        int p = (page != null) ? page : 0;
        int s = (size != null) ? size : 20;

        Page<Note> notesPage = notesService.listNotes(principal.getName(), p, s);
        Page<NoteDTO> dtos = notesPage.map(this::toDTO);

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

    private int parseIfMatchVersion(String ifMatch) {
        if (ifMatch == null || ifMatch.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing If-Match header");
        }
        String v = ifMatch.trim();

        if (v.regionMatches(true, 0, "W/", 0, 2)) {
            v = v.substring(2).trim();
        }
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
