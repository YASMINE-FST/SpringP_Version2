package tn.fst.springproject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.fst.springproject.Entity.Chambre;
import tn.fst.springproject.Repository.BlocRepository;
import tn.fst.springproject.Service.ChambreService;
import tn.fst.springproject.dto.BlocResponseDTO;
import tn.fst.springproject.dto.ChambreRequestDTO;
import tn.fst.springproject.dto.ChambreResponseDTO;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/chambres")
@CrossOrigin(origins = "http://localhost:4200")
public class ChambreController {

    private final ChambreService chambreService;

    public ChambreController(ChambreService chambreService) {
        this.chambreService = chambreService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addChambre(@RequestBody ChambreRequestDTO dto) {
        try {
            Chambre chambre = chambreService.addChambre(dto);
            return ResponseEntity.ok(new ChambreResponseDTO(chambre));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // CORRECTION : retourner les chambres, pas les blocs
    @GetMapping
    public List<ChambreResponseDTO> getAllChambres() {
        return chambreService.getAllChambres();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateChambre(@PathVariable Long id, @RequestBody ChambreRequestDTO dto) {
        try {
            Chambre updated = chambreService.updateChambre(id, dto);
            return ResponseEntity.ok(new ChambreResponseDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChambre(@PathVariable Long id) {
        try {
            chambreService.deleteChambre(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
