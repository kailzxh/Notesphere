package com.example.notesapp.share;

import com.example.notesapp.notes.Note;
import com.example.notesapp.notes.NotesService;
import com.example.notesapp.notes.NoteRepository;
import com.example.notesapp.notes.NoteDTO;
import com.example.notesapp.notes.NotesService;
import org.springframework.stereotype.Service;
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
        note.setShared(true);
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
        Note note = notesService.getNote(share.getNoteId().toString(), share.getNoteId());
        NoteDTO dto = new NoteDTO();
        dto.title = note.getTitle();
        dto.content = note.getContent();
        dto.createdAt = note.getCreatedAt();
        return dto;
    }
}
