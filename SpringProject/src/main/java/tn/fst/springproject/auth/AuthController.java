package tn.fst.springproject.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String sendResetCode(@RequestBody ResetCodeRequest request) {
        System.out.println("✅ Reçu email: " + request.getEmail());

        String code = generateRandomCode();
        resetCodes.put(request.getEmail(), code);  // Stockage du code associé à l'email
        emailService.sendResetCode(request.getEmail(), code);

        System.out.println("📨 Code envoyé à: " + request.getEmail());
        return "Reset code sent to " + request.getEmail();
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
        return ResponseEntity.ok("Code valide");
    }

    private String generateRandomCode() {
        int code = 100000 + new Random().nextInt(900000); // code à 6 chiffres
        return String.valueOf(code);
    }
}
