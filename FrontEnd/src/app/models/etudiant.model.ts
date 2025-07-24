import { Reservation } from './reservation.model';

export interface Etudiant {
  idEtudiant?: number;            // optionnel pour la création
  nomEt: string;
  prenomEt: string;
  cin: number;
  ecole: string;
  dateNaissance: String;            // format Date JS
  email: string;
  password: string;
  role: string;
  reservations?: Reservation[];   // ManyToMany relation, optionnelle
}
