package com.fvthree.blogpost.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank
    @Size(max = 254)
    String username,
    @NotBlank
    @Size(min = 6, max = 40)
    String password){}
