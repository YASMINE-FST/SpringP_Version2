import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-reservation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit {

  // Données sélectionnables
  foyers: any[] = [];
  blocs: any[] = [];
  types: string[] = ['SIMPLE', 'DOUBLE', 'TRIPLE'];


  // Réservations
  reservationsForBloc: any[] = [];
  mesReservations: any[] = [];
  toutesReservations: any[] = [];

  // Contrôles
  isAdmin = false;
  userId = 0;

  // Formulaire de réservation
  reservation = {
    idFoyer: null as number | null,
    idBloc: null as number | null,
    typeC: '',
    dateDebut: '',
    dateFin: ''
  };

  // Chambre trouvée
  chambreDisponible: any = null;

  // Édition
  showEditModal = false;
  editingReservation: any = {};

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // Vérification des droits utilisateur
    const roles = localStorage.getItem('roles');
    this.isAdmin = roles?.includes('ADMIN') || false;
    this.userId = Number(localStorage.getItem('id'));

    // Chargement des données
    this.loadFoyers();

    if (this.isAdmin) {
      this.loadToutesReservations();
    } else {
      this.loadMesReservations();
    }
  }

  // Chargement des foyers
  loadFoyers() {
    this.http.get<any[]>('http://localhost:8080/api/foyers').subscribe(data => {
      this.foyers = data;
    });
  }

  // Quand on change de foyer, on charge les blocs associés
  onFoyerChange() {
    if (this.reservation.idFoyer) {
      this.http.get<any[]>(`http://localhost:8080/api/blocs/byFoyer/${this.reservation.idFoyer}`)
        .subscribe(data => this.blocs = data);
    } else {
      this.blocs = [];
    }
  }

  // Chargement des réservations d'un bloc
  loadBlocReservations() {
    if (this.reservation.idBloc) {
      this.http.get<any[]>(`http://localhost:8080/api/reservations/bloc/${this.reservation.idBloc}`)
        .subscribe(data => this.reservationsForBloc = data);
    }
  }

  // Chargement des réservations personnelles
  loadMesReservations() {
    this.http.get<any[]>(`http://localhost:8080/api/reservations/etudiant/${this.userId}`)
      .subscribe(data => this.mesReservations = data);
  }

  // Chargement des réservations pour admin
  loadToutesReservations() {
    this.http.get<any[]>('http://localhost:8080/api/reservations/admin/all')
      .subscribe(data => {
        console.log('Réservations ADMIN reçues :', data);
        this.toutesReservations = data;
      });
  }

  // Chercher une chambre disponible selon critères
  chercherChambre() {
    if (!this.reservation.idFoyer || !this.reservation.idBloc || !this.reservation.typeC) {
      alert('Veuillez sélectionner un foyer, un bloc et un type de chambre');
      return;
    }

    const params = new HttpParams()
      .set('idFoyer', String(this.reservation.idFoyer))
      .set('idBloc', String(this.reservation.idBloc))
      .set('typeC', this.reservation.typeC);

    this.http.get<any>('http://localhost:8080/api/reservations/chambre-disponible', { params })
      .subscribe(
        res => {
          this.chambreDisponible = res;
          this.loadBlocReservations();
        },
        () => {
          alert('❌ Aucune chambre disponible !');
          this.chambreDisponible = null;
        }
      );
  }

  // Confirmer la réservation
  confirmerReservation() {
    if (!this.chambreDisponible) {
      alert('Veuillez chercher une chambre disponible d’abord.');
      return;
    }

    if (!this.reservation.dateDebut || !this.reservation.dateFin) {
      alert('Veuillez saisir les dates de début et de fin.');
      return;
    }

    const dto = {
      etudiantId: this.userId,
      chambreId: Number(this.chambreDisponible.idChambre),
      foyerId: Number(this.reservation.idFoyer),
      blocId: Number(this.reservation.idBloc),
      typeC: this.reservation.typeC.toUpperCase(),
      dateDebut: this.reservation.dateDebut.split('T')[0],
      dateFin: this.reservation.dateFin.split('T')[0]
    };

    console.log('Payload envoyé:', dto);

    this.http.post('http://localhost:8080/api/reservations', dto).subscribe({
      next: () => {
        alert('✅ Réservation enregistrée !');
        this.resetForm();
        this.loadMesReservations();
      },
      error: err => {
        console.error('Erreur complète:', err);
        alert('❌ Erreur lors de la réservation : ' + err.error);
      }
    });
  }

changerStatut(idReservation: number, nouveauStatut: 'VALIDE' | 'REFUSE') {
  const confirmation = confirm(`Voulez-vous vraiment ${nouveauStatut === 'VALIDE' ? 'valider' : 'refuser'} cette réservation ?`);
  if (!confirmation) return;

  this.http.put(
  `http://localhost:8080/api/reservations/${idReservation}/validation`,
  {}, // corps vide
  {
    params: { statut: nouveauStatut },
    responseType: 'text' as 'json' // <- important pour éviter erreur de parsing
  }
).subscribe({
  next: () => {
    alert(`✅ Réservation ${nouveauStatut === 'VALIDE' ? 'validée' : 'refusée'} avec succès.`);
    this.loadToutesReservations();
  },
  error: err => {
    console.error(err);
    alert('❌ Erreur lors de la mise à jour du statut');
  }
});

}


  // Ouvrir la modale de modification d'une réservation
  editReservation(res: any) {
    if (res.estValide) {
      alert('❌ Cette réservation est déjà validée. Vous ne pouvez pas la modifier.');
      return;
    }

    this.editingReservation = {
      idReservation: res.idReservation,
      dateDebut: this.formatDateForInput(new Date(res.dateDebut)),
      dateFin: this.formatDateForInput(new Date(res.dateFin)),
      etudiantId: res.idEtudiant,
      chambreId: res.chambreId,
      blocId: res.blocId,
      foyerId: res.foyerId,
      typeC: res.typeChambre,
    };
    this.showEditModal = true;
  }

  // Envoyer la mise à jour de la réservation modifiée
  updateReservation() {
    const dto = {
      etudiantId: this.editingReservation.etudiantId,
      chambreId: this.editingReservation.chambreId,
      blocId: this.editingReservation.blocId,
      foyerId: this.editingReservation.foyerId,
      typeC: this.editingReservation.typeC,
      dateDebut: this.editingReservation.dateDebut,
      dateFin: this.editingReservation.dateFin
    };

    this.http.put(`http://localhost:8080/api/reservations/${this.editingReservation.idReservation}`, dto)
      .subscribe({
        next: () => {
          alert('✅ Réservation modifiée avec succès');
          this.closeEditModal();
          this.loadMesReservations();
        },
        error: error => {
          alert('❌ Erreur : ' + (error.error || error.message));
          console.error(error);
        }
      });
  }

  // Annuler une réservation
  annulerReservation(id: number) {
    if (confirm('Voulez-vous vraiment annuler cette réservation ?')) {
      this.http.delete(`http://localhost:8080/api/reservations/${id}`).subscribe({
        next: () => {
          alert('✅ Réservation annulée !');
          this.loadMesReservations();
        },
        error: err => {
          alert('❌ Erreur : ' + (err.error?.message || err.message || 'Erreur inconnue'));
        }
      });
    }
  }

  // Fermer la modale d'édition
  closeEditModal() {
    this.showEditModal = false;
    this.editingReservation = {};
  }

  // Réinitialiser le formulaire de réservation
  resetForm() {
    this.reservation = {
      idFoyer: null,
      idBloc: null,
      typeC: '',
      dateDebut: '',
      dateFin: ''
    };
    this.chambreDisponible = null;
    this.blocs = [];
    this.reservationsForBloc = [];
  }

  // Formatage date pour input type=date (yyyy-MM-dd)
  private formatDateForInput(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  // Nombre de réservations pour la chambre sélectionnée dans le bloc
  getReservationCountForChambre(): number {
    if (!this.chambreDisponible || !this.reservationsForBloc) return 0;
    return this.reservationsForBloc.filter(r =>
      r.chambre?.idChambre === this.chambreDisponible.idChambre
    ).length;
  }

}
