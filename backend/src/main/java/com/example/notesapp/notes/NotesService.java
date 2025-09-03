package com.example.notesapp.notes;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotesService {
    private final NoteRepository noteRepo;

    public NotesService(NoteRepository noteRepo) {
        this.noteRepo = noteRepo;
    }

    // --- Create ---
    public Note createNote(String userId, String title, String content) {
        Note note = new Note();
        note.setUserId(UUID.fromString(userId));
        note.setTitle(title);
        note.setContent(content);
        return noteRepo.save(note);
    }

    // --- List ---
    public Page<Note> listNotes(String userId, int page, int size) {
        return noteRepo.findByUserIdOrderByUpdatedAtDesc(
                UUID.fromString(userId), PageRequest.of(page, size)
        );
    }

    // --- Get (with ownership check) ---
    public Note getNote(String userId, UUID noteId) {
        Note note = noteRepo.findById(noteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!note.getUserId().toString().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return note;
    }

    // --- Get (public, no ownership check) ---
    public Optional<Note> getNoteById(UUID noteId) {
        return noteRepo.findById(noteId);
    }

    // --- Update ---
    @Transactional
    public Note updateNote(String userId, UUID noteId, String title, String content, int ifMatchVersion) {
        Note note = getNote(userId, noteId);
        if (!note.getVersion().equals(ifMatchVersion)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Version conflict");
        }
        note.setTitle(title);
        note.setContent(content);
        note.setUpdatedAt(Instant.now());
        return noteRepo.save(note);
    }

    // --- Delete ---
    public void deleteNote(String userId, UUID noteId) {
        Note note = getNote(userId, noteId);
        noteRepo.delete(note);
    }
}
