package com.example.notesapp.share;

import com.example.notesapp.notes.Note;
import com.example.notesapp.notes.NoteDTO;
import com.example.notesapp.notes.NotesService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class ShareService {
    private final NotesService notesService;
    private final NoteShareRepository shareRepo;

    public ShareService(NotesService notesService, NoteShareRepository shareRepo) {
        this.notesService = notesService;
        this.shareRepo = shareRepo;
    }

    public String createShare(String userId, UUID noteId) {
        Note note = notesService.getNote(userId, noteId);
        String shareId = UUID.randomUUID().toString();

        // mark note as shared
        note.setShared(true);

        // either update existing share or create new
        NoteShare share = shareRepo.findByNoteId(noteId).orElse(new NoteShare());
        share.setNoteId(noteId);
        share.setShareId(shareId);
        shareRepo.save(share);

        return shareId;
    }

    public void revokeShare(String userId, UUID noteId) {
        Note note = notesService.getNote(userId, noteId);
        note.setShared(false);
        shareRepo.deleteByNoteId(noteId);
    }

    public NoteDTO getPublicNote(String shareId) {
        NoteShare share = shareRepo.findByShareId(shareId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // ⚠️ In getNote we need a userId; here we only have noteId.
        // If NotesService requires userId, adjust it; otherwise fetch note directly from repo.
        Note note = notesService.getNoteById(share.getNoteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setUpdatedAt(note.getUpdatedAt());
        dto.setVersion(note.getVersion());
        dto.setShared(note.isShared());

        return dto;
    }
}
