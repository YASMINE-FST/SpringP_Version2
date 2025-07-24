package tn.fst.springproject.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tn.fst.springproject.Entity.Foyer;
import tn.fst.springproject.Service.FoyerService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.fst.springproject.dto.FoyerDTO;
// Ajouter un foyer
import org.springframework.http.MediaType;
import tn.fst.springproject.dto.FoyerRequestDTO;

@RestController
@RequestMapping("/api/foyers")
@CrossOrigin(origins = "http://localhost:4200")
public class FoyerController {

    @Autowired
    private FoyerService foyerService;



    @GetMapping
    public List<FoyerDTO> getAllFoyers() {
        return foyerService.getAllFoyers()
                .stream()
                .map(FoyerDTO::new)
                .collect(Collectors.toList());
    }



    @PostMapping
    public Foyer createFoyer(@RequestBody FoyerRequestDTO dto) {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer(dto.getNomFoyer());
        foyer.setCapaciteFoyer(dto.getCapaciteFoyer());
        // Si besoin, set Univercite ici ou autre champs

        return foyerService.saveFoyer(foyer);
    }


    @PutMapping("/{id}")
    public Foyer updateFoyer(@PathVariable Long id, @RequestBody FoyerRequestDTO dto) {
        Foyer foyer = foyerService.getFoyerById(id);
        if (foyer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Foyer non trouvé");
        }
        foyer.setNomFoyer(dto.getNomFoyer());
        foyer.setCapaciteFoyer(dto.getCapaciteFoyer());
        // Set autres champs si besoin
        return foyerService.updateFoyer(foyer);
    }

    // Supprimer un foyer
    @DeleteMapping("/{id}")
    public void deleteFoyer(@PathVariable Long id) {
        foyerService.deleteFoyer(id);
    }
}
