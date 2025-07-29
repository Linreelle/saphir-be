package com.linreelle.saphir.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString(exclude = "tokens")

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column
    private String middleName;
    @Column(unique = true, nullable = false)
    @Email
    private String email;
    @Column(unique = true)
    private String telephone;
    @Column(nullable = false)
    private String password;
    @Column
    private String confirmPassword;


    @Enumerated(EnumType.STRING)
    private IdentityMeans identityMeans;
    @Column
    private String idCardNumber;
    @Column
    @Lob
    private byte[] idCard;
    @Column
    @Lob
    private byte[] pp;
    @Column
    private LocalDate dateOfBirth;
    @Column
    private String address;
    @Column
    private String profile;

    private LocalDate registeredDate = LocalDate.now();
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Confirmation confirmation;

    private boolean active = true;
    private boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getauthorities();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @PrePersist
    public void prePersist() {
        if (registeredDate == null) {
            registeredDate = LocalDate.now();
        }
        if (dateOfBirth == null) {
            dateOfBirth = LocalDate.now();
        }
        if (role == null) {
            role = Role.USER;
        }
        if (profile == null) {
            profile = firstName + " " + lastName;
        }
    }
}
