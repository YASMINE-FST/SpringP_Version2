import { Chambre } from './chambre.model';
import { Etudiant } from './etudiant.model';

export interface Reservation {
  idReservation: number;
  dateDebut: Date;
  dateFin: Date;
  statut: 'EN_ATTENTE' | 'VALIDE' | 'REFUSE'; // <- corrigé ici
  chambre: Chambre;
  etudiant: Etudiant;
}

