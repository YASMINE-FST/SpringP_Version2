package tn.fst.springproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tn.fst.springproject.Entity.Bloc;

@Data
@Getter
@Setter
public class BlocResponseDTO {
    private Long idBloc;
    private String nomBloc;
    private Long capaciteBloc;
    private String nomFoyer;
    private Long idFoyer;

    public static BlocResponseDTO fromEntity(Bloc bloc) {
        BlocResponseDTO dto = new BlocResponseDTO();
        dto.idBloc = bloc.getIdBloc();
        dto.nomBloc = bloc.getNomBloc();
        dto.capaciteBloc = bloc.getCapaciteBloc();
        dto.nomFoyer = bloc.getFoyer() != null ? bloc.getFoyer().getNomFoyer() : null;
        dto.idFoyer = bloc.getFoyer() != null ? bloc.getFoyer().getIdFoyer() : null;
        return dto;
    }
}
