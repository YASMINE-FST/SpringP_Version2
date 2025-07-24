import { Component, OnInit } from '@angular/core'; 
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Chambre } from '../models/chambre.model';
import { Bloc } from '../models/bloc.model';
import { Foyer } from '../models/foyer.model';
import { ChambreRequest } from '../models/ChambreRequest.model';

interface GroupeType {
  chambres: Chambre[];
  capacite: number;
  nombre: number;
}

interface GroupeBloc {
  bloc: Bloc;
  types: Record<'SIMPLE' | 'DOUBLE' | 'TRIPLE', GroupeType>;
}

@Component({
  selector: 'app-chambre',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chambres.html',
  styleUrls: ['./chambres.css']
})
export class ChambreComponent implements OnInit {

  nombreChambres?: number;
  typeC: 'simple' | 'double' | 'triple' = 'simple';
  idFoyer?: number;
  idBloc?: number;

  foyers: Foyer[] = [];
  blocs: Bloc[] = [];
  blocsFiltres: Bloc[] = [];
  chambres: Chambre[] = [];

  Object = Object;
  objectKeys = Object.keys;

  groupesParBloc: GroupeBloc[] = [];
  types: ('SIMPLE' | 'DOUBLE' | 'TRIPLE')[] = ['SIMPLE', 'DOUBLE', 'TRIPLE'];

  modeEdition = false;
  chambreEnEditionId?: number;

  erreursCapacite: { [blocId: number]: string } = {};
  capaciteParType: Record<'SIMPLE' | 'DOUBLE' | 'TRIPLE', number> = {
    SIMPLE: 1,
    DOUBLE: 2,
    TRIPLE: 3
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadFoyers();
    this.loadBlocs();
    this.loadChambres();
  }

  loadFoyers() {
    this.http.get<Foyer[]>('http://localhost:8080/api/foyers').subscribe({
      next: data => this.foyers = data,
      error: err => console.error('Erreur chargement foyers:', err)
    });
  }

  loadBlocs() {
    this.http.get<Bloc[]>('http://localhost:8080/api/blocs').subscribe({
      next: data => {
        this.blocs = data;
        this.mettreAJourBlocsFiltres();
      },
      error: err => console.error('Erreur chargement blocs:', err)
    });
  }

  loadChambres() {
    this.http.get<Chambre[]>('http://localhost:8080/api/chambres').subscribe({
      next: data => {
        console.log('Chambres reçues :', data);
        this.chambres = data.filter(c =>
          c.idChambre != null &&
          c.typeC != null &&
          c.typeC.trim() !== ''
        );
        this.mettreAJourGroupesParBloc();
        this.verifierCapacites();
      },
      error: err => console.error('Erreur chargement chambres:', err)
    });
  }

  mettreAJourBlocsFiltres() {
    if (this.idFoyer != null) {
      this.blocsFiltres = this.blocs.filter(b => b.idFoyer === this.idFoyer);
    } else {
      this.blocsFiltres = [];
    }
  }

  onFoyerChange(value: number) {
    this.idFoyer = value;
    this.idBloc = undefined;
    this.mettreAJourBlocsFiltres();
  }

  mettreAJourGroupesParBloc() {
    const groupesMap = new Map<number, GroupeBloc>();
    for (const bloc of this.blocs) {
      groupesMap.set(bloc.idBloc!, {
        bloc,
        types: {
          SIMPLE: { chambres: [], capacite: 0, nombre: 0 },
          DOUBLE: { chambres: [], capacite: 0, nombre: 0 },
          TRIPLE: { chambres: [], capacite: 0, nombre: 0 }
        }
      });
    }

    for (const c of this.chambres) {
      const groupe = groupesMap.get(c.idBloc!);
      if (!groupe) continue;

      const type = c.typeC.toUpperCase() as 'SIMPLE' | 'DOUBLE' | 'TRIPLE';
      groupe.types[type].chambres.push(c);
      groupe.types[type].nombre += c.nombreChambre;
      groupe.types[type].capacite += this.capaciteParType[type] * c.nombreChambre;
    }

    this.groupesParBloc = Array.from(groupesMap.values());
  }

  verifierCapacites() {
    this.erreursCapacite = {};
    for (const groupe of this.groupesParBloc) {
      const totalCapacite = Object.values(groupe.types).reduce((sum, t) => sum + t.capacite, 0);
      if (totalCapacite > groupe.bloc.capaciteBloc) {
        this.erreursCapacite[groupe.bloc.idBloc!] = `⚠️ Capacité dépassée dans le bloc "${groupe.bloc.nomBloc}". Max : ${groupe.bloc.capaciteBloc}, actuelle : ${totalCapacite}.`;
      }
    }
  }

  addOuModifierChambre() {
    if (!this.idFoyer || !this.idBloc || !this.nombreChambres || this.nombreChambres <= 0) {
      alert('❗ Tous les champs sont obligatoires.');
      return;
    }

    const typeUpper = this.typeC.toUpperCase() as 'SIMPLE' | 'DOUBLE' | 'TRIPLE';

    // Convertir idBloc en nombre pour éviter la non-correspondance de type
    const blocIdNum = Number(this.idBloc);
    const bloc = this.blocs.find(b => b.idBloc === blocIdNum);
    if (!bloc) {
      alert('❌ Bloc introuvable.');
      return;
    }

    const chambresDansBloc = this.chambres.filter(c => c.idBloc === bloc.idBloc);

    const capaciteExistante = chambresDansBloc
      .filter(c => !this.modeEdition || c.idChambre !== this.chambreEnEditionId)
      .reduce((total, c) => total + this.capaciteParType[c.typeC.toUpperCase() as 'SIMPLE' | 'DOUBLE' | 'TRIPLE'] * c.nombreChambre, 0);

    const capaciteNouvelle = this.capaciteParType[typeUpper] * this.nombreChambres;

    if (capaciteExistante + capaciteNouvelle > bloc.capaciteBloc) {
      alert(`⚠️ Capacité du bloc dépassée ! Max : ${bloc.capaciteBloc}, actuelle : ${capaciteExistante}, ajout : ${capaciteNouvelle}`);
      return;
    }

    const body: ChambreRequest = {
      nombreChambre: this.nombreChambres,
      typeC: typeUpper,
      idBloc: bloc.idBloc
    };

    if (this.modeEdition && this.chambreEnEditionId) {
      this.http.put(`http://localhost:8080/api/chambres/${this.chambreEnEditionId}`, body).subscribe({
        next: () => {
          alert('✅ Chambre modifiée avec succès.');
          this.clearForm();
          this.loadChambres();
        },
        error: err => alert('❌ Erreur : ' + (err.error?.message || JSON.stringify(err.error)))
      });
    } else {
      if (chambresDansBloc.some(c => c.typeC.toUpperCase() === typeUpper)) {
        alert(`❗ Ce bloc possède déjà des chambres de type ${this.typeC}. Modifiez ou supprimez-les.`);
        return;
      }
      this.http.post('http://localhost:8080/api/chambres/add', body).subscribe({
        next: () => {
          alert('✅ Chambre ajoutée avec succès.');
          this.clearForm();
          this.loadChambres();
        },
        error: err => alert('❌ Erreur : ' + (err.error?.message || JSON.stringify(err.error)))
      });
    }
  }

  clearForm() {
    this.nombreChambres = undefined;
    this.typeC = 'simple';
    this.idFoyer = undefined;
    this.idBloc = undefined;
    this.modeEdition = false;
    this.chambreEnEditionId = undefined;
    this.erreursCapacite = {};
    this.blocsFiltres = [];
  }

  supprimerChambre(id: number) {
    if (confirm('❗ Voulez-vous vraiment supprimer cette chambre ?')) {
      this.http.delete(`http://localhost:8080/api/chambres/${id}`).subscribe({
        next: () => {
          alert('🗑️ Chambre supprimée avec succès.');
          this.loadChambres();
        },
        error: () => alert('❌ Erreur lors de la suppression')
      });
    }
  }

  modifierChambre(chambre: Chambre) {
    this.nombreChambres = chambre.nombreChambre;
    this.typeC = chambre.typeC.toLowerCase() as 'simple' | 'double' | 'triple';
    this.idBloc = chambre.idBloc;
    const bloc = this.blocs.find(b => b.idBloc === chambre.idBloc);
    this.idFoyer = bloc?.idFoyer;
    this.modeEdition = true;
    this.chambreEnEditionId = chambre.idChambre;
    this.mettreAJourBlocsFiltres();
  }

  getNomFoyer(idFoyer: number | undefined): string {
    const foyer = this.foyers.find(f => f.idFoyer === idFoyer);
    return foyer ? foyer.nomFoyer : '';
  }

  getGroupeType(type: 'SIMPLE' | 'DOUBLE' | 'TRIPLE', groupe: GroupeBloc): GroupeType {
    return groupe.types[type];
  }

  getErreursCapaciteKeys(): number[] {
    return Object.keys(this.erreursCapacite).map(k => +k);
  }

  getBlocsPourFoyer(): Bloc[] {
    if (!this.idFoyer) return [];
    return this.blocs.filter(b => b.idFoyer === this.idFoyer);
  }
}
