package tn.fst.springproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.springproject.Entity.Bloc;
import tn.fst.springproject.dto.BlocResponseDTO;

public interface BlocRepository extends JpaRepository<Bloc, Long> {



}
