import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { EtudiantService } from '../services/etudiant.service';
import { Etudiant } from '../models/etudiant.model'; 
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule, RouterModule],
  templateUrl: './signup.component.html',
  styleUrls:['./signup.component.css']
})
export class SignupComponent {
  etudiant: Etudiant = {
    nomEt: '',
    prenomEt: '',
    cin: 0,
    ecole: '',
    dateNaissance: '',
    email: '',
    password: '',
    role: 'USER'
  };

  constructor(private etudiantService: EtudiantService, private router: Router) {}

  onSubmit() {
    this.etudiantService.signup(this.etudiant).subscribe({
      next: () => {
        alert('Inscription réussie !');
        this.router.navigate(['/login']);
      },
      error: (err: any) => {
        alert('Erreur lors de l\'inscription');
        console.error(err);
      }
    });
  }

  logout() {
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }
}
