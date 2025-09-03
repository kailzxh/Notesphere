package com.example.notesapp.notes;

import java.time.Instant;
import java.util.UUID;

public class NoteDTO {
    public UUID id;
    public String title;
    public String content;
    public boolean isShared;
    public int version;
    public Instant createdAt;
    public Instant updatedAt;
}
