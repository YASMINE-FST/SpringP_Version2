package tn.fst.springproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlocRequestDTO {
    private long idBloc;
    private String nomBloc;
    private long capaciteBloc;
    private Long idFoyer;
    private String nomFoyer;
}
