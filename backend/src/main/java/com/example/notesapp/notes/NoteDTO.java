package com.example.notesapp.notes;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data

public class NoteDTO {
    private UUID id;
    private String title;
    private String content;
    private boolean shared;
    private Integer version;
    private Instant createdAt;
    private Instant updatedAt;
}
