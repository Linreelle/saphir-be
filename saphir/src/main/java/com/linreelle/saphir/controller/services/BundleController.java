package com.linreelle.saphir.controller.services;


import com.linreelle.saphir.dto.services.BundleResponseDTO;
import com.linreelle.saphir.dto.services.CreateBundleRequestDTO;
import com.linreelle.saphir.dto.validators.CreateBundleValidationGroup;
import com.linreelle.saphir.service.services.BundleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/bundles")
@Tag(name = "Bundle", description = "API for managing bundle")
public class BundleController {
    private final BundleService bundleService;

    @GetMapping
    @Operation(summary = "Get bundles")
    public ResponseEntity<List<BundleResponseDTO>> getBundles(){
        List<BundleResponseDTO> bundles = bundleService.getBundles();
        return ResponseEntity.ok().body(bundles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a bundle")
    public ResponseEntity<BundleResponseDTO> getBundle(
            @PathVariable Integer id){
        BundleResponseDTO bundle = bundleService.getBundle(id);
        return ResponseEntity.ok().body(bundle);
    }

    @PostMapping
    @Operation(summary = "Create a new bundle")
    public ResponseEntity<BundleResponseDTO> createBundle(
            @Validated({Default.class, CreateBundleValidationGroup.class}) @RequestBody CreateBundleRequestDTO createBundleRequestDTO){

        BundleResponseDTO bundleResponseDTO = bundleService.createBundle(createBundleRequestDTO);
        return ResponseEntity.ok().body(bundleResponseDTO);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing bundle")
    public ResponseEntity<BundleResponseDTO> updateBundle(
            @PathVariable Integer id, @Validated({Default.class}) @RequestBody CreateBundleRequestDTO createBundleRequestDTO){

        BundleResponseDTO bundleResponseDTO = bundleService.updateBundle(id, createBundleRequestDTO);
        return ResponseEntity.ok().body(bundleResponseDTO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a bundle")
    public ResponseEntity<Void> deleteBundle(@PathVariable Integer id){
        bundleService.deleteBundle(id);
        return ResponseEntity.noContent().build();
    }
}

