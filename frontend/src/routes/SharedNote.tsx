import { useQuery } from 'react-query';
import { useParams, Navigate } from 'react-router-dom';

export default function SharedNote() {
  const { shareId } = useParams<{ shareId: string }>();

  const { data, error, isLoading } = useQuery(['shared', shareId], async () => {
    if (!shareId) throw new Error('Missing shareId');

    const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/public/notes/${shareId}`);
    if (!res.ok) throw new Error('Note not found');
    return res.json();
  });

  if (isLoading) return <div>Loading...</div>;
  if (error) return <Navigate to="/login" replace />;

  return (
    <div className="p-6 max-w-xl mx-auto bg-white dark:bg-gray-800 rounded shadow">
      <h1 className="text-2xl font-bold mb-2">{data.title}</h1>
      <p>{data.content}</p>
      <p className="text-sm text-gray-500 mt-4">
        Created at: {new Date(data.createdAt).toLocaleString()}
      </p>
    </div>
  );
}
