package com.pro.auth_service.model.entity;

import com.pro.auth_service.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(columnDefinition = "boolean default true")
    private boolean enabled;

    @Column(columnDefinition = "boolean default true")
    private boolean accountNonExpired;

    @Column(columnDefinition = "boolean default true")
    private boolean accountNonLocked;

    @Column(columnDefinition = "boolean default true")
    private boolean credentialsNonExpired;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

}