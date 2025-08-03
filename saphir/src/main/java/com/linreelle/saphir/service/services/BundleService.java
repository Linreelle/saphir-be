package com.linreelle.saphir.service.services;


import com.linreelle.saphir.dto.services.BundleResponseDTO;
import com.linreelle.saphir.dto.services.CreateBundleRequestDTO;
import com.linreelle.saphir.exception.BundleAlreadyExistsException;
import com.linreelle.saphir.exception.BundleNotFindException;
import com.linreelle.saphir.mapper.services.BundleMapper;
import com.linreelle.saphir.model.services.Bundle;
import com.linreelle.saphir.model.services.Offer;
import com.linreelle.saphir.repository.services.BundleRepository;
import com.linreelle.saphir.repository.services.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BundleService {
    private final BundleRepository bundleRepository;
    private final OfferRepository offerRepository;
//    private final BillingServiceGrpcClient billingServiceGrpcClient;
//    private  final KafkaProducer kafkaProducer;


    public List<BundleResponseDTO> getBundles(){
        List<Bundle> bundles = bundleRepository.findAll();
        return bundles.stream().map(BundleMapper::toDTO).toList();
    }
    public BundleResponseDTO getBundle(Integer id){
        Bundle bundle = bundleRepository.findById(id).orElseThrow(
                ()-> new BundleNotFindException("Bundle not find with ID:" +id));
        return BundleMapper.toDTO(bundle);
    }

    public BundleResponseDTO createBundle (CreateBundleRequestDTO createBundleRequestDTO){
        if (bundleRepository.existsByName(createBundleRequestDTO.getName())){
            throw new BundleAlreadyExistsException("A Bundle with this name already exists"
                    + createBundleRequestDTO.getName());
        }

        Bundle newBundle = bundleRepository.save(
                BundleMapper.toModel(createBundleRequestDTO));

//        billingServiceGrpcClient.createBillingAccount(newBundle.getId().toString(),
//                newBundle.getName(), newBundle.getDescription());
//
//        kafkaProducer.sentEvent(newBundle);

        return BundleMapper.toDTO(newBundle);
    }

    public BundleResponseDTO updateBundle(Integer id, CreateBundleRequestDTO createBundleRequestDTO){
        Bundle bundle = bundleRepository.findById(id)
                .orElseThrow(()-> new BundleNotFindException("Bundle not find with ID:" +id));

        if (bundleRepository.existsByNameAndIdNot(createBundleRequestDTO.getName(), id)){
            throw new BundleAlreadyExistsException("A Bundle with this email already exists"
                    + createBundleRequestDTO.getName());
        }

        bundle.setName(createBundleRequestDTO.getName());
        bundle.setDescription(createBundleRequestDTO.getDescription());
        bundle.setOffers(createBundleRequestDTO.getOffers());

        Bundle updatedBundle = bundleRepository.save(bundle);

        return BundleMapper.toDTO(updatedBundle);
    }

    public void addOfferToBundle(Integer bundleId, Offer offer) {
        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new BundleNotFindException("Bundle not found"));
        offer.setBundle(bundle);
        bundle.getOffers().add(offer);
        offerRepository.save(offer);
    }
    public void deleteBundle(Integer id){
        bundleRepository.deleteById(id);
    }

}


