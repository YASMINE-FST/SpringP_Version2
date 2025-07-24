package tn.fst.springproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class SignupRequestDTO {
    // Getters et Setters
    private String nomEt;
    private String prenomEt;
    private long cin;
    private String ecole;
    private Date dateNaissance;
    private String email;
    private String password;

    // Constructeurs
    public SignupRequestDTO() {}

    public SignupRequestDTO(String nomEt, String prenomEt, long cin, String ecole, Date dateNaissance, String email, String password) {
        this.nomEt = nomEt;
        this.prenomEt = prenomEt;
        this.cin = cin;
        this.ecole = ecole;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.password = password;
    }

}
