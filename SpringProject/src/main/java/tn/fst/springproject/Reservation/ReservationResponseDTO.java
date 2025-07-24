package tn.fst.springproject.Reservation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReservationResponseDTO {
    private int idReservation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutReservation statut;
    private long etudiantId;
    private long chambreId;

    private String typeChambre;
    private String blocNom;
    private String foyerNom;
}
