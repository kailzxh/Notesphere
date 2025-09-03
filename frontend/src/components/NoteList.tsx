import { useQuery } from 'react-query';
import client from '../api/client';
import type { NoteDTO } from '../routes/NotesList';

interface NoteListProps {
  notes: NoteDTO[];
  onSelect: (id: string) => void;
}

export default function NoteList({ notes, onSelect }: NoteListProps) {
  return (
    <ul className="divide-y divide-gray-300">
      {notes.map(note => (
        <li key={note.id} className="p-4 hover:bg-gray-100 cursor-pointer" onClick={() => onSelect(note.id)}>
          <div className="font-semibold">{note.title}</div>
          <div className="text-sm text-gray-500">{new Date(note.updatedAt).toLocaleString()}</div>
        </li>
      ))}
    </ul>
  );
}
