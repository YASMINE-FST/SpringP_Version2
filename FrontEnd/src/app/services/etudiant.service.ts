import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Etudiant } from '../models/etudiant.model';
import { LoginResponseDTO } from '../models/LoginResponse';  // <-- import du nouveau modèle

@Injectable({
  providedIn: 'root'
})
export class EtudiantService {
  private baseUrl = 'http://localhost:8080/api/etudiants';

  constructor(private http: HttpClient) {}

  // Login retourne maintenant LoginResponse (email, role, nomComplet)
  login(email: string, password: string): Observable<LoginResponseDTO> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = { email, password };
    return this.http.post<LoginResponseDTO>(`${this.baseUrl}/login`, body, { headers });
  }

  signup(etudiant: Etudiant): Observable<Etudiant> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Etudiant>(`${this.baseUrl}/signup`, etudiant, { headers });
  }
}
