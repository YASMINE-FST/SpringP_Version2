package tn.fst.springproject.dto;

import lombok.Getter;
import lombok.Setter;
import tn.fst.springproject.Entity.Bloc;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class FoyerDTO {

    private long idFoyer;
    private String nomFoyer;
    private long capaciteFoyer;

    // Optionnel: Liste simplifiée des noms de blocs (ou autre info)
    private Set<String> blocsNoms;

    public FoyerDTO() {}

    // Constructeur pour créer un DTO depuis un Foyer
    public FoyerDTO(tn.fst.springproject.Entity.Foyer foyer) {
        this.idFoyer = foyer.getIdFoyer();
        this.nomFoyer = foyer.getNomFoyer();
        this.capaciteFoyer = foyer.getCapaciteFoyer();

        // On récupère juste le nom des blocs pour éviter la récursion
        if (foyer.getBlocs() != null) {
            this.blocsNoms = foyer.getBlocs().stream()
                    .map(Bloc::getNomBloc)
                    .collect(Collectors.toSet());
        }
    }
}
