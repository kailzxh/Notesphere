package com.example.notesapp.user;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private UUID id;
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
    // getters/setters ...
}
