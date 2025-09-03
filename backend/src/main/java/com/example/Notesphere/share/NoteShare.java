package com.example.notesapp.share;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "note_shares")
public class NoteShare {
    @Id @GeneratedValue
    private UUID id;
    @Column(name = "note_id", nullable = false)
    private UUID noteId;
    @Column(name = "share_id", unique = true, nullable = false)
    private String shareId;
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    // getters/setters ...
}
