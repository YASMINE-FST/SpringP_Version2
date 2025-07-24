package tn.fst.springproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.fst.springproject.Entity.Foyer;

@Repository
public interface FoyerRepository extends JpaRepository<Foyer, Long> {
}