import { useState } from 'react';
export default function ShareButton({ shareId }: { shareId: string | null }) {
  const [copied, setCopied] = useState(false);
  const shareUrl = shareId
    ? `${import.meta.env.VITE_PUBLIC_ORIGIN}/shared/${shareId}`
    : '';
  const copyLink = () => {
    if (shareUrl) {
      navigator.clipboard.writeText(shareUrl);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };
  return (
    <button onClick={copyLink} disabled={!shareUrl} className="px-3 py-1 bg-blue-500 text-white rounded">
      {copied ? 'Copied!' : 'Copy Share Link'}
    </button>
  );
}
