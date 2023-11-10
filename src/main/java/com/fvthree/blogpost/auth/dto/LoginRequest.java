package com.fvthree.blogpost.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	
    @NotBlank
    @Size(max = 254)
    private String username;
    

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

}
