package tn.fst.springproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.fst.springproject.Entity.Etudiant;
import tn.fst.springproject.Entity.Foyer;
import tn.fst.springproject.Repository.FoyerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FoyerService {

    @Autowired
    private FoyerRepository foyerRepository;

    public List<Foyer> getAllFoyers() {
        return foyerRepository.findAll();
    }

    public Foyer saveFoyer(Foyer foyer) {
        return foyerRepository.save(foyer);
    }


    public Foyer updateFoyer(Foyer foyer) {
        return foyerRepository.save(foyer);
    }

    public void deleteFoyer(Long id) {
        foyerRepository.deleteById(id);
    }



    public Foyer getFoyerById(Long id) {
        Optional<Foyer> foyerOpt = foyerRepository.findById(id);
        return foyerOpt.orElse(null);
    }




}
