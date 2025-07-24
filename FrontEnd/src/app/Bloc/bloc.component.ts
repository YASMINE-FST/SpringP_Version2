import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-bloc',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bloc.component.html',
  styleUrls: ['./bloc.component.css']
})
export class BlocComponent {
  modeEdition = false;

  nouveauBloc: any = {
    idBloc: null,
    nomBloc: '',
    capaciteBloc: null,
    foyer: null  // objet foyer complet
  };

  blocs: any[] = [];
  foyers: any[] = [];
  message = '';

  constructor(private http: HttpClient) {
    this.loadBlocs();
    this.loadFoyers();
  }

  loadBlocs() {
    this.http.get<any[]>('http://localhost:8080/api/blocs').subscribe({
      next: data => {
        this.blocs = data;
        console.log('Blocs chargés:', this.blocs);
      },
      error: err => {
        console.error('Erreur chargement blocs', err);
      }
    });
  }

  loadFoyers() {
    this.http.get<any[]>('http://localhost:8080/api/foyers').subscribe({
      next: data => {
        this.foyers = data;
        console.log('Foyers chargés:', this.foyers);
      },
      error: err => {
        console.error('Erreur chargement foyers', err);
      }
    });
  }

  getCapaciteTotaleDesBlocs(idFoyer: number): number {
    return this.blocs
      .filter(bloc => bloc.idFoyer === idFoyer)
      .reduce((sum, bloc) => sum + (bloc.capaciteBloc || 0), 0);
  }

  onSubmitBloc() {
    if (!this.nouveauBloc.foyer) {
      alert("❌ Veuillez sélectionner un foyer !");
      return;
    }

    const foyer = this.foyers.find(f => f.idFoyer === this.nouveauBloc.foyer.idFoyer);
    if (!foyer) {
      alert("❌ Foyer non trouvé !");
      return;
    }

    // Vérification doublon nom bloc dans foyer, sauf si édition sur même idBloc
    const doublon = this.blocs.find(b =>
      b.nomBloc.trim().toLowerCase() === this.nouveauBloc.nomBloc.trim().toLowerCase() &&
      b.idFoyer === foyer.idFoyer &&
      (!this.modeEdition || b.idBloc !== this.nouveauBloc.idBloc)
    );

    if (doublon) {
      alert("⚠️ Un bloc avec ce nom existe déjà dans ce foyer !");
      return;
    }

    const capaciteActuelle = this.getCapaciteTotaleDesBlocs(foyer.idFoyer);
    const capaciteDemandee = this.nouveauBloc.capaciteBloc;
    let capaciteTotaleApresAjout = capaciteActuelle;

    if (!this.modeEdition) {
      capaciteTotaleApresAjout += capaciteDemandee;
    } else {
      const blocExistant = this.blocs.find(b => b.idBloc === this.nouveauBloc.idBloc);
      if (blocExistant) {
        capaciteTotaleApresAjout = capaciteActuelle - blocExistant.capaciteBloc + capaciteDemandee;
      }
    }

    if (capaciteTotaleApresAjout > foyer.capaciteFoyer) {
      alert(
        `⚠️ Capacité du foyer dépassée !
Capacité foyer : ${foyer.capaciteFoyer}
Capacité actuelle des blocs : ${capaciteActuelle}
Capacité à ajouter : ${capaciteDemandee}`
      );
      return;
    }

    // Prépare les données à envoyer au backend
    const blocToSend = {
      idBloc: this.nouveauBloc.idBloc,
      nomBloc: this.nouveauBloc.nomBloc,
      capaciteBloc: this.nouveauBloc.capaciteBloc,
      idFoyer: foyer.idFoyer
    };

    if (this.modeEdition) {
      this.http.put(`http://localhost:8080/api/blocs/${this.nouveauBloc.idBloc}`, blocToSend).subscribe({
        next: () => {
          this.message = '✅ Bloc mis à jour avec succès';
          this.resetBlocForm();
          this.loadBlocs();
        },
        error: err => {
          this.message = err.error?.message || "❌ Erreur lors de la mise à jour";
          console.error(err);
        }
      });
    } else {
      this.http.post('http://localhost:8080/api/blocs', blocToSend).subscribe({
        next: () => {
          this.message = '✅ Bloc ajouté avec succès';
          this.resetBlocForm();
          this.loadBlocs();
        },
        error: err => {
          this.message = err.error?.message || "❌ Erreur lors de l'ajout";
          console.error(err);
        }
      });
    }
  }

  modifierBloc(bloc: any) {
    this.modeEdition = true;
    this.nouveauBloc = {
      idBloc: bloc.idBloc,
      nomBloc: bloc.nomBloc,
      capaciteBloc: bloc.capaciteBloc,
      foyer: this.foyers.find(f => f.idFoyer === bloc.idFoyer) || null
    };
  }

  supprimerBloc(idBloc: number) {
    if (confirm('❗ Voulez-vous vraiment supprimer ce bloc ?')) {
      this.http.delete(`http://localhost:8080/api/blocs/${idBloc}`).subscribe({
        next: () => {
          this.message = '✅ Bloc supprimé avec succès';
          this.loadBlocs();
        },
        error: err => {
          this.message = err.error?.message || "❌ Erreur lors de la suppression";
          console.error(err);
        }
      });
    }
  }

  resetBlocForm() {
    this.modeEdition = false;
    this.nouveauBloc = {
      idBloc: null,
      nomBloc: '',
      capaciteBloc: null,
      foyer: null
    };
    this.message = '';
  }
}
