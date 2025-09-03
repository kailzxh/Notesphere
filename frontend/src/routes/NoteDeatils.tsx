import { useState, useEffect } from 'react';
import { useQuery } from 'react-query';
import { useParams, useNavigate } from 'react-router-dom';
import client from '../api/client';
import NoteEditor from '../components/NoteEditor';
import ShareButton from '../components/ShareButton';
import { toast } from 'react-toastify';

export default function NoteDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [note, setNote] = useState<{title: string; content: string; isShared: boolean; version: number; shareId: string | null}>({
    title: '',
    content: '',
    isShared: false,
    version: 0,
    shareId: null,
  });

  const { data, refetch } = useQuery(['note', id], async () => {
    const res = await client.get(`/api/notes/${id}`);
    return res.data;
  }, {
    onSuccess: (data) => {
      setNote({
        title: data.title,
        content: data.content,
        isShared: data.isShared,
        version: data.version,
        shareId: data.shareId || null
      });
    }
  });

  useEffect(() => {
    if (!id) navigate('/notes');
  }, [id, navigate]);

  const saveNote = async () => {
    try {
      const res = await client.put(
        `/api/notes/${id}`,
        { title: note.title, content: note.content },
        { headers: { 'If-Match': `"${note.version}"` } }
      );
      setNote(prev => ({ ...prev, version: res.data.version }));
      toast.success('Saved');
      refetch();
    } catch (e: any) {
      if (e.response?.status === 409) {
        toast.error('Conflict: note was updated elsewhere');
        refetch();
      } else {
        toast.error('Save failed');
      }
    }
  };

  const deleteNote = async () => {
    try {
      await client.delete(`/api/notes/${id}`);
      navigate('/notes');
    } catch (e) {
      toast.error('Delete failed');
    }
  };

  const toggleShare = async () => {
    try {
      if (!note.isShared) {
        const res = await client.post(`/api/notes/${id}/share`);
        setNote(prev => ({ ...prev, isShared: true, shareId: res.data.shareId }));
        toast.success('Note shared');
      } else {
        await client.delete(`/api/notes/${id}/share`);
        setNote(prev => ({ ...prev, isShared: false, shareId: null }));
        toast.success('Share revoked');
      }
    } catch (e) {
      toast.error('Share toggle failed');
    }
  };

  if (!data) return <div>Loading...</div>;

  return (
    <div className="p-6">
      <NoteEditor
        title={note.title}
        content={note.content}
        onChange={(field, val) => setNote({...note, [field]: val})}
      />
      <div className="mt-4 flex space-x-2">
        <button onClick={saveNote} className="bg-green-500 text-white px-3 py-1 rounded">Save</button>
        <button onClick={deleteNote} className="bg-red-500 text-white px-3 py-1 rounded">Delete</button>
        <button onClick={toggleShare} className="px-3 py-1 rounded" 
                style={{ backgroundColor: note.isShared ? '#ef4444' : '#3b82f6', color: 'white' }}>
          {note.isShared ? 'Revoke Share' : 'Share'}
        </button>
        {note.isShared && note.shareId && <ShareButton shareId={note.shareId} />}
      </div>
    </div>
  );
}
