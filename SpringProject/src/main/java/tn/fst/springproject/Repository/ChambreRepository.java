package tn.fst.springproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.fst.springproject.Entity.Chambre;
import tn.fst.springproject.Entity.TypeChambre;

import java.util.List;
import java.util.Optional;

public interface ChambreRepository extends JpaRepository<Chambre, Integer> {

    @Query("SELECT c FROM Chambre c WHERE c.bloc.foyer.idFoyer = :idFoyer AND c.bloc.idBloc = :idBloc AND c.typeC = :typeC AND " +
            "c NOT IN (SELECT r.chambre FROM Reservation r WHERE (r.dateDebut <= CURRENT_DATE AND r.dateFin >= CURRENT_DATE))")
    Optional<Chambre> findChambreDisponible(@Param("idFoyer") Long idFoyer,
                                            @Param("idBloc") Long idBloc,
                                            @Param("typeC") tn.fst.springproject.Entity.TypeChambre typeC);




    @Query("SELECT c FROM Chambre c WHERE c.bloc.foyer.idFoyer = :idFoyer AND c.bloc.idBloc = :idBloc AND c.typeC = :typeC " +
            "AND c NOT IN (SELECT r.chambre FROM Reservation r WHERE r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut)")
    List<Chambre> findChambresDisponiblesBetween(
            @Param("idFoyer") Long idFoyer,
            @Param("idBloc") Long idBloc,
            @Param("typeC") TypeChambre typeC,
            @Param("dateDebut") java.time.LocalDate dateDebut,
            @Param("dateFin") java.time.LocalDate dateFin);

    @Query("SELECT c FROM Chambre c WHERE c.bloc.foyer.idFoyer = :idFoyer AND c.bloc.idBloc = :idBloc AND c.typeC = :typeC")
    List<Chambre> findChambresByFoyerBlocType(@Param("idFoyer") Long idFoyer,
                                              @Param("idBloc") Long idBloc,
                                              @Param("typeC") tn.fst.springproject.Entity.TypeChambre typeC);

}



