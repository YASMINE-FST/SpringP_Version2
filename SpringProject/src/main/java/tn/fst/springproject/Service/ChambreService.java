package tn.fst.springproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.fst.springproject.Entity.Bloc;
import tn.fst.springproject.Entity.Chambre;
import tn.fst.springproject.Entity.TypeChambre;
import tn.fst.springproject.dto.ChambreRequestDTO;
import tn.fst.springproject.Repository.BlocRepository;
import tn.fst.springproject.Repository.ChambreRepository;
import tn.fst.springproject.dto.ChambreResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChambreService {

    private final ChambreRepository chambreRepository;
    private final BlocRepository blocRepository;

    public ChambreService(ChambreRepository chambreRepository, BlocRepository blocRepository) {
        this.chambreRepository = chambreRepository;
        this.blocRepository = blocRepository;
    }

    public Chambre addChambre(ChambreRequestDTO dto) throws Exception {
        Bloc bloc = blocRepository.findById(dto.getIdBloc())
                .orElseThrow(() -> new Exception("Bloc non trouvé"));

        Chambre chambre = new Chambre();
        chambre.setNombreChambre(dto.getNombreChambre());

        chambre.setTypeC(TypeChambre.valueOf(dto.getTypeC().toUpperCase()));
        chambre.setBloc(bloc);

        return chambreRepository.save(chambre);
    }
    public List<ChambreResponseDTO> getAllChambres() {
        return chambreRepository.findAll()
                .stream()
                .map(ChambreResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Chambre updateChambre(Long id, ChambreRequestDTO dto) throws Exception {
        Chambre chambre = chambreRepository.findById(Math.toIntExact(id))  // Remove the cast to int
                .orElseThrow(() -> new Exception("Chambre non trouvée"));

        Bloc bloc = blocRepository.findById(dto.getIdBloc())
                .orElseThrow(() -> new Exception("Bloc non trouvé"));

        chambre.setNombreChambre(dto.getNombreChambre());

        chambre.setTypeC(TypeChambre.valueOf(dto.getTypeC().toUpperCase()));
        chambre.setBloc(bloc);

        return chambreRepository.save(chambre);
    }

    public void deleteChambre(Long id) throws Exception {
        Chambre chambre = chambreRepository.findById(Math.toIntExact(id))  // Remove the cast to int
                .orElseThrow(() -> new Exception("Chambre non trouvée"));
        chambreRepository.delete(chambre);
    }



    public Chambre trouverChambreDisponible(Long idFoyer, Long idBloc, String typeC) {
        try {
            TypeChambre typeEnum = TypeChambre.valueOf(typeC.toUpperCase());
            return chambreRepository.findChambreDisponible(idFoyer, idBloc, typeEnum).orElse(null);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de chambre invalide : " + typeC);
        }
    }


}
