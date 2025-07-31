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

  emailExists = false;
  isSubmitting = false;
  signupSuccessMessage: string | null = null;

  constructor(private etudiantService: EtudiantService, private router: Router) {}

  onSubmit() {
    if (this.isSubmitting) return;

    this.isSubmitting = true;
    this.etudiantService.signup(this.etudiant).subscribe({
      next: () => {
        this.signupSuccessMessage = `Un email de confirmation a été envoyé à ${this.etudiant.email}. Veuillez vérifier votre boîte mail pour confirmer votre inscription.`;
        this.isSubmitting = false;
        this.etudiant = {
          nomEt: '',
          prenomEt: '',
          cin: 0,
          ecole: '',
          dateNaissance: '',
          email: '',
          password: '',
          role: 'USER'
        };
        this.emailExists = false;
      },
      error: (err: any) => {
        alert('Erreur lors de l\'inscription : ' + (err.error?.error || 'Erreur inconnue'));
        console.error(err);
        this.isSubmitting = false;
      }
    });
  }

  checkEmail() {
    if (!this.etudiant.email) {
      this.emailExists = false;
      return;
    }
    this.etudiantService.checkEmail(this.etudiant.email).subscribe({
      next: (res) => {
        this.emailExists = res.exists;
      },
      error: () => {
        this.emailExists = false;
      }
    });
  }

  logout() {
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }

  showPassword = false;

togglePasswordVisibility() {
  this.showPassword = !this.showPassword;
}

}
