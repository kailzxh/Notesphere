package com.example.notesapp;

import com.example.notesapp.notes.NotesService;
import com.example.notesapp.notes.Note;
import com.example.notesapp.notes.NoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;
import org.springframework.web.server.ResponseStatusException;

@DataJpaTest
public class NotesServiceTest {

    @Autowired
    NoteRepository repo;

    NotesService service;

    @BeforeEach
    void setUp() {
        service = new NotesService(repo);
    }

    @Test
    void testCreateAndUpdateNote() {
        String userId = UUID.randomUUID().toString();

        // Create note
        Note note = service.createNote(userId, "Title", "Content");
        assertThat(note.getId()).isNotNull();
        assertThat(note.getVersion()).isNotNull();

        // Update with correct version
        Note updated = service.updateNote(userId, note.getId(), "New", "Data", note.getVersion());
        assertThat(updated.getTitle()).isEqualTo("New");
        assertThat(updated.getContent()).isEqualTo("Data");

        // Simulate version conflict (pass an outdated version)
        int wrongVersion = updated.getVersion() - 1;
        assertThatThrownBy(() ->
                service.updateNote(userId, note.getId(), "Fail", "Again", wrongVersion)
        )
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Version conflict");
    }
}
