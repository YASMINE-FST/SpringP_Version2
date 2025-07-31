package tn.fst.springproject.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.fst.springproject.Entity.Etudiant;
import tn.fst.springproject.auth.EmailService;
import tn.fst.springproject.dto.LoginRequestDTO;
import tn.fst.springproject.dto.LoginResponseDTO;
import tn.fst.springproject.dto.SignupRequestDTO;

import java.util.Map;

@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin(origins = "http://localhost:4200")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;
    @Autowired
    private EmailService emailService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequest) {
        if (etudiantService.findByEmail(signupRequest.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email déjà utilisé"));
        }

        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt(signupRequest.getNomEt());
        etudiant.setPrenomEt(signupRequest.getPrenomEt());
        etudiant.setCin(signupRequest.getCin());
        etudiant.setEcole(signupRequest.getEcole());
        etudiant.setDateNaissance(signupRequest.getDateNaissance());
        etudiant.setEmail(signupRequest.getEmail());
        etudiant.setPassword(signupRequest.getPassword());
        etudiant.setEnabled(false);

        // Token de confirmation
        String token = java.util.UUID.randomUUID().toString();
        etudiant.setConfirmationToken(token);


        System.out.println("Avant encodage : " + etudiant.getPassword());
        Etudiant saved = etudiantService.saveEtudiant(etudiant);
        System.out.println("Après encodage : " + saved.getPassword());


        // Envoie de l'email
        String confirmationUrl = "http://localhost:4200/confirm-email?token=" + token;
        String subject = "Confirmation de votre inscription";
        String body = "Bonjour " + etudiant.getNomEt()+etudiant.getPrenomEt() + ",\n\n"
                + "Cliquez sur ce lien pour confirmer votre inscription :\n" + confirmationUrl;

        emailService.sendSimpleMessage(etudiant.getEmail(), subject, body);

        return ResponseEntity.ok(Map.of("message", "Un email de confirmation a été envoyé à " + etudiant.getEmail()));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        Etudiant existing = etudiantService.findByEmail(loginRequest.getEmail());

        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect");
        }

        if (!existing.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Compte non activé. Veuillez confirmer votre email.");
        }

        System.out.println("Mot de passe reçu (clair) : " + loginRequest.getPassword());
        System.out.println("Mot de passe hashé en base : " + existing.getPassword());
        System.out.println("Résultat comparaison : " + passwordEncoder.matches(loginRequest.getPassword(), existing.getPassword()));
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("123456789");
        System.out.println(hash);


        boolean matches = passwordEncoder.matches(loginRequest.getPassword(), existing.getPassword());
        if (matches) {
            LoginResponseDTO dto = new LoginResponseDTO();
            dto.setId(existing.getId());
            dto.setEmail(existing.getEmail());
            dto.setRole(existing.getRole());
            dto.setNom(existing.getNomEt() + " " + existing.getPrenomEt());
            return ResponseEntity.ok(dto);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect");
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@RequestParam String email) {
        boolean exists = etudiantService.findByEmail(email) != null;
        return ResponseEntity.ok(Map.of("exists", exists));
    }




    @GetMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
        Etudiant etudiant = etudiantService.findByConfirmationToken(token);

        if (etudiant == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Token invalide"));
        }

        etudiant.setEnabled(true);
        etudiant.setConfirmationToken(null);

        // Save directly using repository to avoid password re-encoding
        etudiantService.saveEtudiantWithoutEncoding(etudiant);

        return ResponseEntity.ok(Map.of("message", "Inscription confirmée. Vous pouvez maintenant vous connecter."));
    }


}