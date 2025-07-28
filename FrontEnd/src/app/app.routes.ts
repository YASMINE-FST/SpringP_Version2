import { Routes } from '@angular/router';

// Standalone components
import { MainLayoutComponent } from './layouts/main-layout/main-layout';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout';
import { LoginComponent } from './Login/login.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { ReservationComponent } from './reservations/reservation.component';
import { ChambreComponent } from './ChambresComponent/chambre.component';
import { BlocComponent } from './Bloc/bloc.component';
import { DashboardComponent } from './dashboard/dashboard';
import { ConfirmEmailComponent } from './confirm-email/confirm_email.component';
export const routes: Routes = [
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', component: LoginComponent },
      { path: 'signup', loadComponent: () => import('./Signup/signup.component').then(m => m.SignupComponent) },
      { path: 'reset-password', component: ResetPasswordComponent },
       { path: 'confirm-email', component: ConfirmEmailComponent }
      
    ]
  },
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: 'foyer', loadComponent: () => import('./foyer-list/foyer-list.component').then(m => m.FoyerListComponent) },
      { path: 'reservations', component: ReservationComponent },
      { path: 'Chambres', component: ChambreComponent },
      { path: 'Blocs', component: BlocComponent },
      { path: 'dashboard', component: DashboardComponent },
    ]
  },
  { path: '**', redirectTo: 'login' },
];
