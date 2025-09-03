package com.example.notesapp;

import com.example.notesapp.notes.NotesService;
import com.example.notesapp.notes.Note;
import com.example.notesapp.notes.NoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

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
        Note note = service.createNote(userId, "Title", "Content");
        assertThat(note.getId()).isNotNull();
        assertThat(note.getVersion()).isEqualTo(0);

        // Update with correct version
        Note updated = service.updateNote(userId, note.getId(), "New", "Data", note.getVersion());
        assertThat(updated.getTitle()).isEqualTo("New");

        // Simulate version conflict
        assertThatThrownBy(() -> {
            service.updateNote(userId, note.getId(), "Fail", "Again", 0);
        }).isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Version conflict");
    }
}
