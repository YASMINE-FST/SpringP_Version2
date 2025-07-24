import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true, // ← needed if you're using standalone components
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'] // ← typo fix: "styleUrl" → "styleUrls"
})
export class DashboardComponent implements OnInit {
  dashboardData: any;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get('/api/dashboard').subscribe({
      next: (data) => {
        this.dashboardData = data;
      },
      error: (err) => {
        console.error('Dashboard fetch failed:', err);
      }
    });
  }
}
