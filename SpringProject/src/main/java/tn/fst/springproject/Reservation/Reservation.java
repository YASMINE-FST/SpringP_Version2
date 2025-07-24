package tn.fst.springproject.Reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import tn.fst.springproject.Entity.Chambre;
import tn.fst.springproject.Entity.Etudiant;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Reservation")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReservation")
    private int idReservation;


    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;
;

    private StatutReservation statut ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chambre_id")
    @JsonBackReference
    private Chambre chambre;



}
