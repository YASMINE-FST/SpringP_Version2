package tn.fst.springproject.Reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequestDTO {
    private Long etudiantId;
    private Long chambreId; // facultatif si tu utilises foyerId + blocId + typeC pour trouver la chambre
    private Long foyerId;
    private Long blocId;
    private String typeC; // Exemple : "SIMPLE"
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;

}
