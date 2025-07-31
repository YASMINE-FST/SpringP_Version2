package tn.fst.springproject.Reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.fst.springproject.Entity.Chambre;
import tn.fst.springproject.Entity.Etudiant;
import tn.fst.springproject.Repository.ChambreRepository;
import tn.fst.springproject.User.EtudiantRepository;
import tn.fst.springproject.Entity.TypeChambre;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ChambreRepository chambreRepository;
    private final EtudiantRepository etudiantRepository;

    public Reservation reserver(ReservationRequestDTO dto) {

        Etudiant etudiant = etudiantRepository.findById(dto.getEtudiantId())
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        // Vérifie que l'étudiant n’a pas déjà réservé une chambre dans cette période
        List<Reservation> existing = reservationRepository
                .findByEtudiantIdAndDateOverlap(etudiant.getId(), dto.getDateDebut(), dto.getDateFin());

        if (!existing.isEmpty()) {
            throw new RuntimeException("L'étudiant a déjà une réservation pour cette période.");
        }

        Chambre chambre = trouverChambreDisponible(dto.getFoyerId(), dto.getBlocId(), dto.getTypeC());

        if (chambre == null) {
            throw new RuntimeException("Aucune chambre disponible");
        }

        Reservation reservation = new Reservation();
        reservation.setChambre(chambre);
        reservation.setEtudiant(etudiant);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setStatut(StatutReservation.EN_ATTENTE);

        return reservationRepository.save(reservation);
    }

    public Chambre trouverChambreDisponible(Long foyerId, Long blocId, String typeC) {
        TypeChambre typeChambreEnum = TypeChambre.valueOf(typeC.toUpperCase());

        List<Chambre> chambres = chambreRepository
                .findChambresByFoyerBlocType(foyerId, blocId, typeChambreEnum);

        for (Chambre chambre : chambres) {
            int maxCapacite = switch (chambre.getTypeC()) {
                case SIMPLE -> 1;
                case DOUBLE -> 2;
                case TRIPLE -> 3;
            };

            int nbOccupants = reservationRepository
                    .countActiveReservationsForChambre((long) chambre.getIdChambre(), LocalDate.now());

            if (nbOccupants < maxCapacite) {
                return chambre;
            }
        }

        return null;
    }


    public List<ReservationResponseDTO> getReservationsByEtudiant(Long id) {
        return reservationRepository.findByEtudiantId(id)
                .stream()
                .map(res -> new ReservationResponseDTO(
                        res.getIdReservation(),
                        res.getDateDebut(),
                        res.getDateFin(),
                        res.getStatut(),
                        res.getEtudiant().getId(),
                        res.getChambre().getIdChambre(),
                        res.getChambre().getTypeC().name(),
                        res.getChambre().getBloc().getNomBloc(),
                        res.getChambre().getBloc().getFoyer().getNomFoyer()
                )).collect(Collectors.toList());
    }

    public List<Reservation> getReservationsByBloc(Long blocId) {
        return reservationRepository.findByChambre_Bloc_IdBloc(blocId);
    }

    public Reservation updateReservation(Long id, ReservationUpdateDTO dto) {
        Reservation res = reservationRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        res.setDateDebut(dto.getDateDebut());
        res.setDateFin(dto.getDateFin());
        return reservationRepository.save(res);
    }

    public void changerStatutReservation(Long id, StatutReservation statut) {
        Reservation res = reservationRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        res.setStatut(statut);
        reservationRepository.save(res);
    }

    public void annulerReservation(Integer id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationAdminDTO> getAllForAdmin() {
        return reservationRepository.findAll().stream()
                .map(ReservationAdminDTO::from)
                .collect(Collectors.toList());

    }





}
