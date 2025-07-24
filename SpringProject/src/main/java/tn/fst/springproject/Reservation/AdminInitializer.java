package tn.fst.springproject.Reservation;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tn.fst.springproject.Entity.Etudiant;
import tn.fst.springproject.Repository.EtudiantRepository;

import java.text.SimpleDateFormat;

@Component
public class AdminInitializer {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdminIfNotExists() {
        // Vérifie si un admin existe déjà
        if (etudiantRepository.findByEmail("admin@gmail.com") == null) {
            Etudiant admin = new Etudiant();
            admin.setNomEt("Admin");
            admin.setPrenomEt("Root");
            admin.setCin(99999999L);
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // mot de passe hashé
            admin.setEcole("FST");
            admin.setRole("ADMIN");

            try {
                admin.setDateNaissance(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
            } catch (Exception e) {
                throw new RuntimeException("Date invalide", e);
            }

            etudiantRepository.save(admin);
            System.out.println("✔ Admin ajouté automatiquement.");
        } else {
            System.out.println("ℹ Admin déjà existant, rien à faire.");
        }
    }
}
