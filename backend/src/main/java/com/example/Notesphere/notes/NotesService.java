package com.example.Notesphere.notes;

import com.example.notesapp.common.ApiError;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class NotesService {
    private final NoteRepository noteRepo;
    public NotesService(NoteRepository noteRepo) {
        this.noteRepo = noteRepo;
    }

    public Note createNote(String userId, String title, String content) {
        Note note = new Note();
        note.setUserId(UUID.fromString(userId));
        note.setTitle(title);
        note.setContent(content);
        return noteRepo.save(note);
    }

    public Page<Note> listNotes(String userId, int page, int size) {
        return noteRepo.findByUserIdOrderByUpdatedAtDesc(
                UUID.fromString(userId), PageRequest.of(page, size)
        );
    }

    public Note getNote(String userId, UUID noteId) {
        Note note = noteRepo.findById(noteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!note.getUserId().toString().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return note;
    }

    @Transactional
    public Note updateNote(String userId, UUID noteId, String title, String content, int ifMatchVersion) {
        Note note = getNote(userId, noteId);
        if (note.getVersion() != ifMatchVersion) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Version conflict");
        }
        note.setTitle(title);
        note.setContent(content);
        note.setUpdatedAt(Instant.now());
        return noteRepo.save(note);
    }

    public void deleteNote(String userId, UUID noteId) {
        Note note = getNote(userId, noteId);
        noteRepo.delete(note);
    }
}
