package tn.fst.springproject.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.fst.springproject.Reservation.Reservation;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {


    @Query("SELECT r FROM Reservation r WHERE r.etudiant.id = :etudiantId " +
            "AND (r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut)")
    List<Reservation> findByEtudiantIdAndDateOverlap(@Param("etudiantId") Long idEtudiant,
                                                     @Param("dateDebut") LocalDate dateDebut,
                                                     @Param("dateFin") LocalDate dateFin);


    // Compter les réservations actives dans une chambre à une date donnée (ex: maintenant)
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.chambre.idChambre = :chambreId " +
            "AND r.dateDebut <= :date AND r.dateFin >= :date")
    int countActiveReservationsForChambre(Long chambreId, LocalDate date);

    // Toutes les réservations pour un bloc donné
    List<Reservation> findByChambre_Bloc_IdBloc(Long idBloc);

    // Par étudiant
    List<Reservation> findByEtudiantId(Long etudiantId);
}
