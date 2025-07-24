package tn.fst.springproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private Long id; // ✅ ajouter ceci
    private String email;
    private String role;
    private String nom;
}
