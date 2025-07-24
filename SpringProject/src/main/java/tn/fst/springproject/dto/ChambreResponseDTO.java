package tn.fst.springproject.dto;

import lombok.Getter;
import lombok.Setter;
import tn.fst.springproject.Entity.Bloc;
import tn.fst.springproject.Entity.Chambre;
import tn.fst.springproject.Entity.Foyer;

@Getter
@Setter
public class ChambreResponseDTO {
    private long idChambre;
    private long nombreChambre; // <- remplacé ici
    private String typeC;
    private Long idBloc;
    private String nomBloc;
    private Long idFoyer;
    private String nomFoyer;

    public ChambreResponseDTO(Chambre chambre) {
        this.idChambre = chambre.getIdChambre();
        this.nombreChambre = chambre.getNombreChambre(); // <- et ici
        this.typeC = chambre.getTypeC().name();

        Bloc bloc = chambre.getBloc();
        if (bloc != null) {
            this.idBloc = bloc.getIdBloc();
            this.nomBloc = bloc.getNomBloc();

            Foyer foyer = bloc.getFoyer();
            if (foyer != null) {
                this.idFoyer = foyer.getIdFoyer();
                this.nomFoyer = foyer.getNomFoyer();
            }
        }
    }
}
