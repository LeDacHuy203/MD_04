package com.example.userservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String id;
    private String username;
    private String token;
    private LocalDateTime timestamp;
}
