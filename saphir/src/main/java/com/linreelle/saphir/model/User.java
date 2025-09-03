package com.linreelle.saphir.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.linreelle.saphir.model.services.Bundle;
import com.linreelle.saphir.model.services.Offer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Data
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
    @Column
    private String title;
    @Email
    private String email;
    @Column(unique = true)
    private String telephone;
    @Column(nullable = false)
    private String password;
    private String createdBy;
    @Column
    private String confirmPassword;


    @Enumerated(EnumType.STRING)
    private IdType idType;
    @Column
    private String idCardNumber;
    @Column
    @Lob
    private byte[] idCard;
    @Column
    @Lob
    private byte[] profilePicture;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Column
    private String address;
    @Column
    private String profile;

    private LocalDate registeredDate = LocalDate.now();
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Token> tokens;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private Confirmation confirmation;

    @ManyToMany
    @JoinTable(
            name = "user_offer_subscription",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id")
    )
    @ToString.Exclude
    private Set<Offer> subscribedOffers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_bundle_subscription",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "bundle_id")
    )
    @ToString.Exclude
    private Set<Bundle> subscribedBundles = new HashSet<>();

    private boolean active = true;
    private boolean enabled = false;
    private boolean isSystemUser = false;
    @Column(name = "hasadhere")
    private boolean hasAdhere = false;
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
        if (createdBy == null) {
            createdBy = firstName + " " + lastName;
        }
    }
}
