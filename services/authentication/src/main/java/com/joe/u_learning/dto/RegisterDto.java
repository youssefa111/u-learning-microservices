package com.joe.u_learning.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.NumberFormat;

public record RegisterDto(@NotBlank @Size(max = 50) String username,
                          @NotBlank @Size(max = 50) String password ,
                          @NotBlank @Size(max = 70) @Email String email ,
                          @NotBlank @Size(max = 120) String address ,
                          @NumberFormat(style = NumberFormat.Style.NUMBER) @Size(max = 11) String phoneNumber
) {
}
