package com.fvthree.blogpost.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    @Size(max = 254)
    private String username;
    
    @NotBlank
    @Size(max = 254)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 56)
    private String password;
    
}
