package tn.fst.springproject.auth;

import lombok.Data;

@Data
public class VerifyCodeRequest {
    private String email;
    private String code;
}
