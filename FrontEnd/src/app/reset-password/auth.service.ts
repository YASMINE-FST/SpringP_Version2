import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// ✅ Interfaces for clean typing
export interface ApiResponse {
  message: string;
}

export interface LoginResponse {
  token: string;
  roles: string[];
  id: number;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  // 🔐 Connexion normale
  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, {
      email,
      password,
    });
  }

  // 📧 Envoi du code de réinitialisation par email
sendResetCode(email: string): Observable<ApiResponse> {
  return this.http.post<ApiResponse>(`${this.apiUrl}/send-reset-code`, { email });
}


  // ✔️ Vérification du code saisi par l'utilisateur
  verifyResetCode(email: string, code: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.apiUrl}/verify-reset-code`, { email, code });
  }

  // 🔄 Réinitialisation du mot de passe
  resetPassword(email: string, newPassword: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.apiUrl}/reset-password`, {
      email,
      newPassword,
    });
  }

  // 🔓 Déconnexion
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('roles');
    localStorage.removeItem('id');
  }
}
