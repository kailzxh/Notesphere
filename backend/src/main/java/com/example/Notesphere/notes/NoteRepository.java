package com.example.notesapp.notes;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {
    Page<Note> findByUserIdOrderByUpdatedAtDesc(UUID userId, Pageable pageable);
}
