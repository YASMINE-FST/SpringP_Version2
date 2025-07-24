import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class NavbarComponent implements OnInit {
  roles: string | null = null;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.roles = localStorage.getItem('roles');
  }

  isAdmin(): boolean {
    return this.roles?.includes('ADMIN') || false;
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
import { OnInit } from '@angular/core';