import client from '../api/client';
import { setAccessToken, clearAccessToken } from './auth';

export async function initAuth() {
  try {
    const res = await client.post('/auth/refresh');
    setAccessToken(res.data.accessToken);
  } catch (err) {
    clearAccessToken();
  }
}
