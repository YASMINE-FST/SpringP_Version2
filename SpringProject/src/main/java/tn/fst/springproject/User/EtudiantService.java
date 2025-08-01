package tn.fst.springproject.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.fst.springproject.Entity.Etudiant;
import tn.fst.springproject.auth.EmailService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public Etudiant saveEtudiant(Etudiant etudiant) {
        if (etudiant.getRole() == null) {
            etudiant.setRole("USER");
        }

        String pwd = etudiant.getPassword();
        if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$")) {
            etudiant.setPassword(passwordEncoder.encode(pwd));
        }
        return etudiantRepository.save(etudiant);
    }


    public Etudiant findByEmail(String email) {
        return etudiantRepository.findByEmail(email);
    }



    public void sendResetCode(String email) {
        Etudiant etudiant = etudiantRepository.findByEmail(email);
        if (etudiant == null) {
            throw new RuntimeException("Email non trouvé");
        }

        String code = String.format("%06d", new Random().nextInt(999999));
        etudiant.setResetCode(code);
        etudiant.setResetCodeExpiry(Date.from(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant()));
        etudiantRepository.save(etudiant);

        emailService.sendResetCode(email, code); // ✅ correct

    }

    public boolean verifyResetCode(String email, String code) {
        Etudiant etudiant = etudiantRepository.findByEmail(email);
        if (etudiant == null) {
            throw new RuntimeException("Email introuvable");
        }

        return etudiant.getResetCode() != null
                && etudiant.getResetCode().equals(code)
                && etudiant.getResetCodeExpiry().after(new Date());
    }

    public void resetPassword(String email, String newPassword) {
        Etudiant etudiant = etudiantRepository.findByEmail(email);
        if (etudiant == null) {
            throw new RuntimeException("Email introuvable");
        }

        etudiant.setPassword(passwordEncoder.encode(newPassword));
        etudiant.setResetCode(null);
        etudiant.setResetCodeExpiry(null);
        etudiantRepository.save(etudiant);
    }

    public Etudiant findByConfirmationToken(String token) {
        return etudiantRepository.findByConfirmationToken(token).orElse(null);
    }



    public Etudiant saveEtudiantWithoutEncoding(Etudiant etudiant) {
        // Just save the entity as is, no encoding here
        return etudiantRepository.save(etudiant);
    }



}
