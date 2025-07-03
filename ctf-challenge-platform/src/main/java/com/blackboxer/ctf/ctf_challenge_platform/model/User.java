package com.blackboxer.ctf.ctf_challenge_platform.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ctf_user") // <-- Adicione esta linha!
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password; // pode criptografar depois
}


