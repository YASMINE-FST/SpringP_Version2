import { Component, OnDestroy } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { EtudiantService } from '../services/etudiant.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy {
  email: string = '';
  password: string = '';
  rememberMe: boolean = false;
  message: string = '';
  isError: boolean = false;
  loading: boolean = false;
  
  private loginSubscription?: Subscription;

  constructor(
    private etudiantService: EtudiantService,
    private router: Router
  ) {}

onSubmit(): void {
  if (this.loading) return;

  this.loading = true;
  this.message = '';

  console.log('Submitting login with', { email: this.email, password: this.password });

  this.loginSubscription = this.etudiantService.login(this.email, this.password).subscribe({
    next: (response) => {
      console.log('Login successful response:', response);
      this.handleLoginSuccess(response);
    },
    error: (error) => {
      console.error('Login error:', error);
      this.handleLoginError(error);
    },
    complete: () => {
      this.loading = false;
    }
  });
}


  loginWithGoogle(): void {
    // Implémentez la logique Google OAuth ici
    this.showMessage('Google login will be implemented soon', false);
  }

  loginWithApple(): void {
    // Implémentez la logique Apple OAuth ici
    this.showMessage('Apple login will be implemented soon', false);
  }

private handleLoginSuccess(response: any): void {
  if (this.rememberMe) {
    this.storeCredentials();
  }

  // ✅ Stocker ID et rôle dans le localStorage
  localStorage.setItem('id', response.id);
  localStorage.setItem('roles', response.role); // ✅ correspond au code du ReservationComponent


  this.showMessage('Login successful!', false);

  // Rediriger selon le rôle
  if (response.role === 'ADMIN') {
    this.router.navigate(['/foyer']);
  } else {
    this.router.navigate(['/reservations']);
  }
}



  private handleLoginError(error: any): void {
    this.loading = false;
    
    if (error.status === 401 || error.status === 404) {
      this.showMessage('Invalid email or password', true);
    } else if (error.status === 0) {
      this.showMessage('Server connection error', true);
    } else {
      this.showMessage(`Server error (${error.status})`, true);
    }
    
    console.error('Login error:', error);
  }

  private storeCredentials(): void {
    localStorage.setItem('rememberedEmail', this.email);
  }

  showMessage(msg: string, isError: boolean): void {
    this.message = msg;
    this.isError = isError;
    
    if (!isError) {
      setTimeout(() => this.message = '', 3000);
    } else {
      setTimeout(() => this.message = '', 5000);
    }
  }

  forgotPassword(event: Event): void {
    event.preventDefault();
    this.router.navigate(['/reset-password']);
  }

  ngOnDestroy(): void {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
  }

  showPassword = false ;

togglePasswordVisibility() {
  this.showPassword = !this.showPassword;
}

}