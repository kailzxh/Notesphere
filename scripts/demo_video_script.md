# Demo Video Script

1. **Sign Up:** Open the app, go to Sign Up, enter email/password, submit. (Show success toast)
2. **Login:** Logout if needed, then log in with the new account. (Show storing token, refresh token cookie set)
3. **Create Note:** Click "New Note". Title it, write content, click Save. (Show note appears in list)
4. **Edit Note:** Click the note, modify content, click Save. (Show updatedAt change)
5. **Optimistic Locking:** (Optional) In another tab change the note (simulate), then try saving original (should show Conflict message).
6. **Share Note:** Click Share, copy link, open incognito and paste. (Show note readonly in incognito)
7. **Revoke Share:** Back in app, click "Revoke Share". Try link again (should 404).
8. **Logout:** Click Logout. (Token cookie cleared, show cannot access protected route)

*Record at 720p, 30fps, H.264 MP4. Keep file ≤10MB by limiting recording time.*
