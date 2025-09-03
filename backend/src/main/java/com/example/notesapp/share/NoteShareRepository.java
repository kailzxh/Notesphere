package com.example.notesapp.share;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface NoteShareRepository extends JpaRepository<NoteShare, UUID> {
    Optional<NoteShare> findByShareId(String shareId);
    Optional<NoteShare> findByNoteId(UUID noteId);
    void deleteByNoteId(UUID noteId);
}
