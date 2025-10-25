package com.budget_blitz.users;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "token", unique = true)
    private String token;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}