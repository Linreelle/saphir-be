package com.linreelle.saphir.model.services;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    // Add other fields as needed

    @ManyToMany(mappedBy = "offers")
    @JsonBackReference
    private Set<Bundle> bundles = new HashSet<>();

    private String createdBy;
    private String updatedBy;
}
