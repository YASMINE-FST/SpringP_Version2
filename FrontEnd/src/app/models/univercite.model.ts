import { Foyer } from './foyer.model';
export interface Univercite {
  idUniversite: number;
  nomUniversite: string;
  adresse: string;
  foyer?: Foyer[]; // optionnel pour éviter la récursivité infinie
}
