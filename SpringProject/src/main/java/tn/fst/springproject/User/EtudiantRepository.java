package tn.fst.springproject.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.fst.springproject.Entity.Etudiant;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Etudiant findByEmail(String email);
    Optional<Etudiant> findByConfirmationToken(String token);



}