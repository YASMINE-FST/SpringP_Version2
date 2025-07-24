import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Foyer } from '../models/foyer.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FoyerService {
  private apiUrl = 'http://localhost:8080/api/foyers';

  constructor(private http: HttpClient) {}

  getAllFoyers(): Observable<Foyer[]> {
    return this.http.get<Foyer[]>(this.apiUrl);
  }

  saveFoyer(foyer: Foyer): Observable<Foyer> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Foyer>(this.apiUrl, foyer, { headers });
  }

  updateFoyer(foyer: Foyer): Observable<Foyer> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<Foyer>(`${this.apiUrl}/${foyer.idFoyer}`, foyer, { headers });
  }

  deleteFoyer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
