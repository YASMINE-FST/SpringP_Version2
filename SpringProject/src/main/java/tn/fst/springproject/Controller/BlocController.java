package tn.fst.springproject.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.fst.springproject.Service.BlocService;
import tn.fst.springproject.dto.BlocRequestDTO;
import tn.fst.springproject.dto.BlocResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/blocs")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class BlocController {

    private final BlocService blocService;

    @GetMapping
    public List<BlocResponseDTO> getAll() {
        return blocService.getAllBlocs();
    }

    @PostMapping
    public BlocResponseDTO ajouterBloc(@RequestBody BlocRequestDTO dto) throws Exception {
        return blocService.addBloc(dto);
    }

    @PutMapping("/{id}")
    public BlocResponseDTO updateBloc(@PathVariable Long id, @RequestBody BlocRequestDTO dto) throws Exception {
        return blocService.updateBloc(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteBloc(@PathVariable Long id) throws Exception {
        blocService.deleteBloc(id);
    }

    @GetMapping("/byFoyer/{idFoyer}")
    public List<BlocResponseDTO> getBlocsByFoyer(@PathVariable Long idFoyer) throws Exception {
        return blocService.getBlocsByFoyer(idFoyer);
    }

}
