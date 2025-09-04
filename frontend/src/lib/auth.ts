// auth.ts

let accessToken: string | null = null;

export function setAccessToken(token: string) {
  accessToken = token;
  localStorage.setItem('accessToken', token); // persist
}

export function getAccessToken(): string | null {
  if (!accessToken) {
    accessToken = localStorage.getItem('accessToken');
  }
  return accessToken;
}

export function clearAccessToken() {
  accessToken = null;
  localStorage.removeItem('accessToken');
}
