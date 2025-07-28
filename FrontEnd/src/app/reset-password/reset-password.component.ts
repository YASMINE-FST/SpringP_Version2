import { Component, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
  imports: [CommonModule, FormsModule],
})
export class ResetPasswordComponent {
  email = '';
  code = '';
  newPassword = '';
  step = 1;
  message = '';
  isError = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}


  goToLogin() {
    this.router.navigate(['/login']); // ajuste le chemin selon ta route login
  }
sendCode() {
  this.authService.sendResetCode(this.email).subscribe({
    next: (res) => {
      console.log("Réponse du backend:", res);
      this.step = 2;
      this.showMessage('Un code a été envoyé à votre adresse email', false);
    },
    error: (err) => {
      console.error("Erreur lors de l'envoi:", err);
      // Si backend renvoie un message précis sur email non trouvé, on l'affiche
      const errorMsg = err?.error?.error || "Erreur lors de l'envoi de l'email";
      this.showMessage(errorMsg, true);
    }
  });
}

verifyCode() {
  console.log('Envoi vérification code:', { email: this.email, code: this.code });
  this.authService.verifyResetCode(this.email, this.code).subscribe({
    next: () => {
      this.step = 3;
      this.showMessage('Code vérifié, saisissez un nouveau mot de passe', false);
    },
    error: (err) => {
      console.error('Erreur verification code:', err);
      const errorMsg = err?.error?.error || 'Code invalide';
      this.showMessage(errorMsg, true);
    }
  });
}


  resetPassword() {
    this.authService.resetPassword(this.email, this.newPassword).subscribe({
      next: () => {
        this.showMessage('Mot de passe modifié avec succès', false);
        this.cdr.detectChanges();
        setTimeout(() => this.router.navigate(['/login']), 3000);
      },
      error: () => {
        this.showMessage('Erreur lors du changement de mot de passe', true);
        this.cdr.detectChanges();
      }
    });
  }

  private showMessage(msg: string, isError: boolean) {
    this.message = msg;
    this.isError = isError;
    setTimeout(() => {
      this.message = '';
      this.cdr.detectChanges();
    }, 5000);
  }
}
