import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Etudiant } from '../models/etudiant.model';
import { LoginResponseDTO } from '../models/LoginResponse'; 
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EtudiantService {
  private baseUrl = 'http://localhost:8080/api/etudiants';
  private apiUrl = 'http://localhost:8080/api/etudiants';

  constructor(private http: HttpClient) {}

login(email: string, password: string): Observable<LoginResponseDTO> {
  const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  const body = { email, password };
  return this.http.post<LoginResponseDTO>(`${this.baseUrl}/login`, body, { headers })
    .pipe(
      catchError(error => {
        console.error('Login failed', error);
        return throwError(() => error);
      })
    );
}

  signup(etudiant: Etudiant): Observable<Etudiant> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Etudiant>(`${this.baseUrl}/signup`, etudiant, { headers });
  }

  checkEmail(email: string) {
  return this.http.get<{ exists: boolean }>(
    `http://localhost:8080/api/etudiants/check-email?email=${email}`
  );
}

confirmEmail(token: string) {
  return this.http.get(`${this.apiUrl}/confirm-email?token=${token}`);
}

}
