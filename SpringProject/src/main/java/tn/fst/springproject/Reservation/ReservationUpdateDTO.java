package tn.fst.springproject.Reservation;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationUpdateDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
