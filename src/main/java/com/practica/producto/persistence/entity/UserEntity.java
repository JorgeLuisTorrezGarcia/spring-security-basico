package com.practica.producto.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_No_Expired")
    private boolean accountNoExpired;  // lo pide spring security

    @Column(name = "accounto_Locked")
    private boolean accountLocked;      // lo pide spring security

    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired;// lo pide spring security

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)  //EAGER = va cagar los datos antes(sobre carga de datos)
    @JoinTable(name = "user_roles" , joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RolesEntity> roles = new HashSet<>(); //usamos set por q no queremos que se repitan los roles
}
