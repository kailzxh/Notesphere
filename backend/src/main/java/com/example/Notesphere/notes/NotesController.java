package com.example.notesapp.notes;

import com.example.notesapp.common.ApiError;
import com.example.notesapp.common.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
public class NotesController {
    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteUpdateDTO body, Principal principal) {
        Note note = notesService.createNote(principal.getName(), body.title, body.content);
        NoteDTO dto = toDTO(note);
        return ResponseEntity.created(URI.create("/api/notes/" + note.getId())).body(dto);
    }

    @GetMapping
    public ApiResponse listNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Principal principal) {
        var notesPage = notesService.listNotes(principal.getName(), page, size);
        var dtos = notesPage.map(this::toDTO);
        return new ApiResponse(dtos.getContent(), notesPage.getNumber(), notesPage.getSize(), notesPage.getTotalElements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNote(@PathVariable UUID id, Principal principal) {
        Note note = notesService.getNote(principal.getName(), id);
        NoteDTO dto = toDTO(note);
        return ResponseEntity.ok()
                .eTag("\"" + note.getVersion() + "\"")
                .body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(
            @PathVariable UUID id,
            @RequestHeader("If-Match") String ifMatch,
            @RequestBody NoteUpdateDTO body,
            Principal principal) {
        int version;
        try {
            version = Integer.parseInt(ifMatch.replace("\"", ""));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid If-Match header");
        }
        Note note = notesService.updateNote(principal.getName(), id, body.title, body.content, version);
        NoteDTO dto = toDTO(note);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id, Principal principal) {
        notesService.deleteNote(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }

    private NoteDTO toDTO(Note note) {
        NoteDTO dto = new NoteDTO();
        dto.id = note.getId();
        dto.title = note.getTitle();
        dto.content = note.getContent();
        dto.isShared = note.isShared();
        dto.version = note.getVersion();
        dto.createdAt = note.getCreatedAt();
        dto.updatedAt = note.getUpdatedAt();
        return dto;
    }
}
