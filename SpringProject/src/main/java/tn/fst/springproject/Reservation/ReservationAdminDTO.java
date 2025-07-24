package tn.fst.springproject.Reservation;

import lombok.Data;
import tn.fst.springproject.Reservation.Reservation;


import java.time.LocalDate;

@Data
public class ReservationAdminDTO {
    private Integer idReservation;
    private String etudiantNom;
    private String etudiantEmail;
    private String foyerNom;
    private String blocNom;
    private Long chambreId;
    private String typeChambre;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutReservation statut;

    public static ReservationAdminDTO from(Reservation r) {
        ReservationAdminDTO dto = new ReservationAdminDTO();

        dto.setIdReservation(r.getIdReservation());
        if (r.getEtudiant() != null) {
            dto.setEtudiantNom(r.getEtudiant().getNomEt());
            dto.setEtudiantEmail(r.getEtudiant().getEmail());
        }
        if (r.getChambre() != null) {
            dto.setChambreId((long) r.getChambre().getIdChambre());
            dto.setTypeChambre(r.getChambre().getTypeC().toString());
            if (r.getChambre().getBloc() != null) {
                dto.setBlocNom(r.getChambre().getBloc().getNomBloc());
                if (r.getChambre().getBloc().getFoyer() != null) {
                    dto.setFoyerNom(r.getChambre().getBloc().getFoyer().getNomFoyer());
                }
            }
        }

        dto.setDateDebut(r.getDateDebut());
        dto.setDateFin(r.getDateFin());

        // Ici on récupère le statut sous forme de String
        dto.setStatut(r.getStatut());


        return dto;
    }}
