package tn.fst.springproject.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.fst.springproject.Entity.Etudiant;
import tn.fst.springproject.User.EtudiantService;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private EmailService emailService;

    private ConcurrentHashMap<String, String> resetCodes = new ConcurrentHashMap<>();

    public static class ResetCodeRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class VerifyCodeRequest {
        private String email;
        private String code;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    @PostMapping("/send-reset-code")
    public ResponseEntity<?> sendResetCode(@RequestBody ResetCodeRequest request) {
        System.out.println("✅ Reçu email: " + request.getEmail());

        Etudiant etudiant = etudiantService.findByEmail(request.getEmail());
        if (etudiant == null) {
            // Email non trouvé : on retourne une erreur 400 ou un message générique
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email non trouvé dans la base de données"));
        }

        String code = generateRandomCode();
        resetCodes.put(request.getEmail(), code);
        emailService.sendResetCode(request.getEmail(), code);

        System.out.println("📨 Code envoyé à: " + request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Code envoyé à " + request.getEmail()));
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestBody VerifyCodeRequest request) {
        System.out.println("✅ Vérification code pour email: " + request.getEmail() + ", code: " + request.getCode());

        String expectedCode = resetCodes.get(request.getEmail());
        if (expectedCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Aucun code demandé pour cet email\"}");
        }
        if (!expectedCode.equals(request.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Code invalide\"}");
        }

        // Code correct -> on peut supprimer le code stocké
        resetCodes.remove(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Code valide"));

    }



    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        String email = request.getEmail();
        String newPassword = request.getNewPassword();

        System.out.println("🔄 Réinitialisation pour: " + email + ", nouveau mot de passe: " + newPassword);

        Etudiant etudiant = etudiantService.findByEmail(email);
        if (etudiant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Utilisateur non trouvé"));
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        etudiant.setPassword(encodedPassword);
        etudiantService.saveEtudiant(etudiant);

        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
    }

    private String generateRandomCode() {
        int code = 100000 + new Random().nextInt(900000); // code à 6 chiffres
        return String.valueOf(code);
    }
}
