/*import { Univercite } from './univercite.model';
import { Bloc } from './bloc.model';

export interface Foyer {
  id: number;
  nomFoyer: string;
  capaciteFoyer: number;
  univercite?: Univercite;  // facultatif, car ça peut être null
  blocs?: Bloc[];           // idem
}*/


export interface Foyer {
  idFoyer?: number;  // optionnel, car généré côté serveur
  nomFoyer: string;
  capaciteFoyer: number;
}
