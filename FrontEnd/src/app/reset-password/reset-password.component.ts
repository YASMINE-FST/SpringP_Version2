import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from './auth.service'; // Adjust path as necessary

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent {
  email = '';
  code = '';
  newPassword = '';
  step = 1;
  message = '';
  isError = false;
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  goToLogin() {
    this.router.navigate(['/login']);
  }

  sendCode() {
    if (this.loading) return;
    this.loading = true;

    this.authService.sendResetCode(this.email).subscribe({
      next: () => {
        this.step = 2;
        this.showMessage('Un code a été envoyé à votre adresse email', false);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.showMessage(err?.error?.error || "Erreur lors de l'envoi de l'email", true);
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  verifyCode() {
    if (this.loading) return;
    this.loading = true;

    this.authService.verifyResetCode(this.email, this.code).subscribe({
      next: () => {
        this.step = 3;
        this.showMessage('Code vérifié, saisissez un nouveau mot de passe', false);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.showMessage(err?.error?.error || 'Code invalide', true);
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  resetPassword() {
    if (this.loading) return;
    this.loading = true;

    this.authService.resetPassword(this.email, this.newPassword).subscribe({
      next: () => {
        this.showMessage('Mot de passe modifié avec succès', false);
        this.loading = false;
        this.cdr.detectChanges();
        setTimeout(() => this.router.navigate(['/login']), 3000);
      },
      error: () => {
        this.showMessage('Erreur lors du changement de mot de passe', true);
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  private showMessage(msg: string, isError: boolean) {
    this.message = msg;
    this.isError = isError;
    this.cdr.detectChanges();
    setTimeout(() => {
      this.message = '';
      this.cdr.detectChanges();
    }, 5000);
  }
}
