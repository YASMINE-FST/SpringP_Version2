package tn.fst.springproject.Controller; // attention à la casse !

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.fst.springproject.Entity.Etudiant;
import tn.fst.springproject.Service.EtudiantService;
import tn.fst.springproject.dto.LoginRequestDTO;
import tn.fst.springproject.dto.LoginResponseDTO;
import tn.fst.springproject.dto.SignupRequestDTO;

@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin(origins = "http://localhost:4200")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @PostMapping("/signup")
    public Etudiant signup(@RequestBody SignupRequestDTO signupRequest) {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt(signupRequest.getNomEt());
        etudiant.setPrenomEt(signupRequest.getPrenomEt());
        etudiant.setCin(signupRequest.getCin());
        etudiant.setEcole(signupRequest.getEcole());
        etudiant.setDateNaissance(signupRequest.getDateNaissance());
        etudiant.setEmail(signupRequest.getEmail());
        etudiant.setPassword(signupRequest.getPassword());
        return etudiantService.saveEtudiant(etudiant);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody Etudiant etudiant) {
        Etudiant existing = etudiantService.findByEmail(etudiant.getEmail());

        if (existing != null && passwordEncoder.matches(etudiant.getPassword(), existing.getPassword())) {
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


}
