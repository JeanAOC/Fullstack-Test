import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { tap } from 'rxjs/operators';

interface DecodedToken {
  exp: number;
  role: string;
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    return this.http.post<{ accessToken: string; refreshToken: string }>(`${this.apiUrl}/login`, { username, password });
  }

  register(data: { username: string; password: string; email: string }) {
    return this.http.post(`${this.apiUrl}/register`, data);
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('jwtToken');
    return !!token && !this.isTokenExpired(token);
  }

  isAdmin(): boolean {
    const token = localStorage.getItem('jwtToken');
    if (!token) return false;
    const decoded = jwtDecode<DecodedToken>(token);
    return decoded.role === 'ROLE_ADMIN';
  }

  private isTokenExpired(token: string): boolean {
    const decoded = jwtDecode<DecodedToken>(token);
    return decoded.exp * 1000 < Date.now();
  }

  logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('refreshToken');
    this.router.navigate(['/login']);
  }

  refreshToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      this.logout();
      return;
    }

    return this.http.post<{ accessToken: string; refreshToken: string }>(`${this.apiUrl}/refresh-token`, { refreshToken }).pipe(
      tap((response: { accessToken: string; refreshToken: string }) => {
        localStorage.setItem('jwtToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);
      })
    );
  }

  getCurrentUser(): DecodedToken | null {
    const token = localStorage.getItem('jwtToken');
    if (!token) return null;
    return jwtDecode<DecodedToken>(token);
  }
}