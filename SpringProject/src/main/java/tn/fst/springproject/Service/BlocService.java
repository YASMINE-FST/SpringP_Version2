package tn.fst.springproject.Service;

import org.springframework.stereotype.Service;
import tn.fst.springproject.Entity.Bloc;
import tn.fst.springproject.Entity.Foyer;
import tn.fst.springproject.Repository.BlocRepository;
import tn.fst.springproject.Repository.FoyerRepository;
import tn.fst.springproject.dto.BlocRequestDTO;
import tn.fst.springproject.dto.BlocResponseDTO;

import java.util.List;

@Service
public class BlocService {

    private final BlocRepository blocRepository;
    private final FoyerRepository foyerRepository;

    public BlocService(BlocRepository blocRepository, FoyerRepository foyerRepository) {
        this.blocRepository = blocRepository;
        this.foyerRepository = foyerRepository;
    }

    public List<BlocResponseDTO> getAllBlocs() {
        List<Bloc> blocs = blocRepository.findAll();
        return blocs.stream().map(BlocResponseDTO::fromEntity).toList();
    }

    public BlocResponseDTO addBloc(BlocRequestDTO dto) throws Exception {
        Foyer foyer = foyerRepository.findById(dto.getIdFoyer())
                .orElseThrow(() -> new Exception("Foyer non trouvé"));

        Bloc bloc = new Bloc();
        bloc.setNomBloc(dto.getNomBloc());
        bloc.setCapaciteBloc(dto.getCapaciteBloc());
        bloc.setFoyer(foyer);

        Bloc saved = blocRepository.save(bloc);

        return BlocResponseDTO.fromEntity(saved);
    }

    public BlocResponseDTO updateBloc(Long id, BlocRequestDTO dto) throws Exception {
        Bloc bloc = blocRepository.findById(id)
                .orElseThrow(() -> new Exception("Bloc non trouvé"));

        Foyer foyer = foyerRepository.findById(dto.getIdFoyer())
                .orElseThrow(() -> new Exception("Foyer non trouvé"));

        bloc.setNomBloc(dto.getNomBloc());
        bloc.setCapaciteBloc(dto.getCapaciteBloc());
        bloc.setFoyer(foyer);

        Bloc updated = blocRepository.save(bloc);

        return BlocResponseDTO.fromEntity(updated);
    }

    public void deleteBloc(Long id) throws Exception {
        if (!blocRepository.existsById(id)) {
            throw new Exception("Bloc non trouvé");
        }
        blocRepository.deleteById(id);
    }

    public List<BlocResponseDTO> getBlocsByFoyer(Long idFoyer) throws Exception {
        var foyer = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new Exception("Foyer non trouvé"));

        var blocs = blocRepository.findAll().stream()
                .filter(bloc -> bloc.getFoyer().getIdFoyer().equals(idFoyer))
                .toList();

        return blocs.stream().map(BlocResponseDTO::fromEntity).toList();
    }

}
