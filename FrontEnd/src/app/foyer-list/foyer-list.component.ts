import { Component, OnInit } from '@angular/core';
import { FoyerService } from '../services/foyer.service';
import { Foyer } from '../models/foyer.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-foyer-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './foyer-list.component.html',
  styleUrls: ['./foyer-list.component.css']
})
export class FoyerListComponent implements OnInit {

  foyers: Foyer[] = [];
  nouveauFoyer: Foyer = { nomFoyer: '', capaciteFoyer: 0 };
  modeEdition: boolean = false;
  foyerEnEditionId?: number;

  constructor(private foyerService: FoyerService, private router: Router) {}

ngOnInit() {
  this.chargerFoyers();
}



  chargerFoyers() {
    this.foyerService.getAllFoyers().subscribe(data => {
      console.log("Foyers récupérés :", data);
      this.foyers = data;
    });
  }

  onSubmit() {
    const nomFoyerSaisi = this.nouveauFoyer.nomFoyer.trim().toLowerCase();

    const existeDeja = this.foyers.some(f =>
      f.nomFoyer.trim().toLowerCase() === nomFoyerSaisi &&
      (!this.modeEdition || f.idFoyer !== this.foyerEnEditionId)
    );

    if (existeDeja) {
      alert('⚠️ Un foyer avec ce nom existe déjà !');
      return;
    }

    if (this.modeEdition && this.foyerEnEditionId != null) {
      const foyerModifie: Foyer = {
        idFoyer: this.foyerEnEditionId,
        nomFoyer: this.nouveauFoyer.nomFoyer,
        capaciteFoyer: this.nouveauFoyer.capaciteFoyer
      };

      this.foyerService.updateFoyer(foyerModifie).subscribe(() => {
        alert("✅ Foyer mis à jour avec succès !");
        this.resetForm();
        this.chargerFoyers();
      });
    } else {
      this.foyerService.saveFoyer(this.nouveauFoyer).subscribe(() => {
        alert("✅ Foyer ajouté avec succès !");
        this.resetForm();
        this.chargerFoyers();
      });
    }
  }

  lireFoyer(foyer: Foyer) {
    this.nouveauFoyer = { nomFoyer: foyer.nomFoyer, capaciteFoyer: foyer.capaciteFoyer };
    this.modeEdition = false;
    this.foyerEnEditionId = foyer.idFoyer;
  }

  modifierFoyer(foyer: Foyer) {
    this.nouveauFoyer = { ...foyer };
    this.modeEdition = true;
    this.foyerEnEditionId = foyer.idFoyer;
  }

  supprimerFoyer(id?: number) {
    if (!id) return;

    if (confirm('❗ Voulez-vous vraiment supprimer ce foyer ?')) {
      this.foyerService.deleteFoyer(id).subscribe(() => {
        alert("🗑️ Foyer supprimé avec succès !");
        this.foyers = this.foyers.filter(f => f.idFoyer !== id);
      }, error => {
        alert("❌ Erreur lors de la suppression !");
        console.error("Erreur lors de la suppression :", error);
      });
    }
  }

  resetForm() {
    this.nouveauFoyer = { nomFoyer: '', capaciteFoyer: 0 };
    this.modeEdition = false;
    this.foyerEnEditionId = undefined;
  }

  logout() {
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }

  
}
