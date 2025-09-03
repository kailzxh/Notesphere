interface NoteEditorProps {
  title: string;
  content: string;
  onChange: (field: 'title' | 'content', value: string) => void;
}
export default function NoteEditor({ title, content, onChange }: NoteEditorProps) {
  return (
    <div className="space-y-2">
      <input
        type="text"
        value={title}
        onChange={e => onChange('title', e.target.value)}
        placeholder="Title"
        className="w-full border rounded px-2 py-1"
      />
      <textarea
        value={content}
        onChange={e => onChange('content', e.target.value)}
        placeholder="Content"
        className="w-full border rounded px-2 py-1 h-40"
      />
    </div>
  );
}
