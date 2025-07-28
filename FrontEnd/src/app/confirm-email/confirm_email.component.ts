import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EtudiantService } from '../services/etudiant.service';

@Component({
  selector: 'app-confirm-email',
  standalone: true,
  template: `<div>Confirmation en cours...</div>`,
})
export class ConfirmEmailComponent implements OnInit {
  token: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private etudiantService: EtudiantService,
    private router: Router
  ) {}

  ngOnInit() {
    this.token = this.route.snapshot.queryParamMap.get('token');

    if (this.token) {
      this.etudiantService.confirmEmail(this.token).subscribe({
        next: () => {
          alert('Inscription réussie !');
          // Rediriger vers la page d’accueil (ajuste la route si besoin)
          this.router.navigate(['/']);
        },
        error: (err) => {
          alert('Erreur lors de la confirmation : ' + (err.error?.error || 'Token invalide'));
          this.router.navigate(['/']);
        }
      });
    } else {
      alert('Token manquant.');
      this.router.navigate(['/']);
    }
  }
}
