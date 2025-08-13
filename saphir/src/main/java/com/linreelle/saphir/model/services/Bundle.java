package com.linreelle.saphir.model.services;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bundles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bundle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    @ManyToMany
    @JoinTable(
            name = "bundle_offer",
            joinColumns = @JoinColumn(name = "bundle_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id")
    )
    @JsonManagedReference
    private Set<Offer> offers = new HashSet<>();

    private String createdBy;
    private String updatedBy;
}
