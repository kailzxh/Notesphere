import { useState } from 'react';
import { useQuery, useQueryClient } from 'react-query';
import client from '../api/client';
import NoteList from '../components/NoteList';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

export interface NoteDTO {
  id: string;
  title: string;
  content: string;
  isShared: boolean;
  version: number;
  createdAt: string;
  updatedAt: string;
}

export default function NotesList() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [page] = useState(0);

  const { data, isLoading, error } = useQuery(['notes', page], async () => {
    const res = await client.get(`/api/notes?page=${page}&size=20`);
    return res.data;
  });

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error loading notes</div>;

  const notes: NoteDTO[] = data.items;

  const handleCreate = async () => {
    try {
      await client.post('/api/notes', { title: 'New Note', content: '' });
      queryClient.invalidateQueries('notes');
    } catch (e) {
      toast.error('Failed to create note');
    }
  };

  return (
    <div className="p-6">
      <button onClick={handleCreate} className="mb-4 bg-blue-500 text-white px-3 py-1 rounded">
        + New Note
      </button>
      <NoteList notes={notes} onSelect={id => navigate(`/notes/${id}`)} />
    </div>
  );
}
