import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth'; // If your backend runs locally on 8080


  constructor(private http: HttpClient) {}

  // 🔐 Connexion normale
  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, {
      email,
      password,
    });
  }

  // 📧 Envoi du code de réinitialisation par email
sendResetCode(email: string): Observable<any> {
  return this.http.post(`${this.apiUrl}/send-reset-code`, { email }, {
    headers: { 'Content-Type': 'application/json' },
    responseType: 'text'  // <--- ajouter ça pour dire que la réponse est texte
  });
}

  // ✔️ Vérification du code saisi par l'utilisateur
verifyResetCode(email: string, code: string): Observable<any> {
  return this.http.post(`${this.apiUrl}/verify-reset-code`, { email, code }, {
    headers: { 'Content-Type': 'application/json' }
  });
}


  // 🔄 Réinitialisation du mot de passe avec le nouveau
  resetPassword(email: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset-password`, {
      email,
      newPassword,
    });
  }

  

  // 🔓 Déconnexion (facultatif)
  logout(): void {
    localStorage.removeItem('id');
    localStorage.removeItem('roles');
    localStorage.removeItem('token'); // si token utilisé
  }
}
