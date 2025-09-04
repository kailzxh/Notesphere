package com.example.notesapp.share;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "note_shares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteShare {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "note_id", nullable = false)
    private UUID noteId;

    @Column(name = "share_id", unique = true, nullable = false)
    private String shareId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
