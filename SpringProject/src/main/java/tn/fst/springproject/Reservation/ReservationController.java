package tn.fst.springproject.Reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.fst.springproject.Entity.Chambre;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> reserverChambre(@RequestBody ReservationRequestDTO dto) {
        try {
            Reservation reservation = reservationService.reserver(dto);
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @GetMapping("/chambre-disponible")
    public ResponseEntity<?> chercherChambreDisponible(
            @RequestParam Long idFoyer,
            @RequestParam Long idBloc,
            @RequestParam String typeC) {

        Chambre chambre = reservationService.trouverChambreDisponible(idFoyer, idBloc, typeC);

        if (chambre != null) {
            return ResponseEntity.ok(chambre);
        }
        return ResponseEntity.status(404).body("Aucune chambre disponible");
    }

    @GetMapping("/etudiant/{id}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByEtudiant(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationsByEtudiant(id));
    }

    @GetMapping("/bloc/{id}")
    public ResponseEntity<List<Reservation>> getReservationsByBloc(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationsByBloc(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(
            @PathVariable long id,
            @RequestBody ReservationUpdateDTO dto) {
        try {
            Reservation updated = reservationService.updateReservation(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @PutMapping("/{id}/validation")
    public ResponseEntity<?> validerOuRefuser(@PathVariable Long id, @RequestParam String statut) {
        try {
            StatutReservation statutEnum = StatutReservation.valueOf(statut.toUpperCase());
            reservationService.changerStatutReservation((long) Math.toIntExact(id), statutEnum);
            return ResponseEntity.ok("✅ Réservation mise à jour");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ Statut invalide");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<ReservationAdminDTO>> getAllReservationsForAdmin() {
        return ResponseEntity.ok(reservationService.getAllForAdmin());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> annulerReservation(@PathVariable Long id) {
        reservationService.annulerReservation(Math.toIntExact(id));
        return ResponseEntity.ok().build();
    }
}
