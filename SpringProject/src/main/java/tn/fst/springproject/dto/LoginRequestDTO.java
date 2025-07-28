package tn.fst.springproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data

public class LoginRequestDTO {
    private String email;
    private String password;

    // getters et setters
}
