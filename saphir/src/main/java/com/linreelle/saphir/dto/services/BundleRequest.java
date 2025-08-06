package com.linreelle.saphir.dto.services;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class BundleRequest {
    private String name;
    private String description;
    private Set<Long> offerIds;

}
