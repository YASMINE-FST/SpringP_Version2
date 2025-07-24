package tn.fst.springproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChambreRequestDTO {
    private long nombreChambre;
    private String typeC;
    private Long idBloc;

    public ChambreRequestDTO() {
    }
}
