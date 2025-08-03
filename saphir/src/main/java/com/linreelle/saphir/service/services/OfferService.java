package com.linreelle.saphir.service.services;


import com.linreelle.saphir.dto.services.OfferRequestDTO;
import com.linreelle.saphir.dto.services.OfferResponseDTO;
import com.linreelle.saphir.exception.BundleAlreadyExistsException;
import com.linreelle.saphir.exception.BundleNotFindException;
import com.linreelle.saphir.mapper.services.OfferMapper;
import com.linreelle.saphir.model.User;
import com.linreelle.saphir.model.services.Offer;
import com.linreelle.saphir.model.services.Subscription;
import com.linreelle.saphir.repository.UserRepository;
import com.linreelle.saphir.repository.services.OfferRepository;
import com.linreelle.saphir.repository.services.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public List<OfferResponseDTO> getOffers(){

        log.debug("Fetching offers from repository");
        List<Offer> offers = offerRepository.findByIsActiveTrue();
        log.info("Received request to fetch offers");
        log.debug("Offer list size: {}", offers.size());
        offers.forEach(o -> log.debug("Mapping offer: {}", o.getId()));

        return offers.stream().map(OfferMapper::toDTO).toList();
    }
    public void subscribeUserToOffer(UUID userId, Integer offerId) {
        Optional<User> user = userRepository.findById(userId);
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(()-> new BundleNotFindException("No offer found with id: " + offerId));
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setOffer(offer);

        subscriptionRepository.save(subscription);
    }
    public OfferResponseDTO getOffer(Integer id){
        Offer offer = offerRepository.findById(id).orElseThrow(
                ()-> new BundleNotFindException("Offer not find with ID:" +id));
        return OfferMapper.toDTO(offer);
    }
    public List<Subscription> getUserSubscriptions(UUID userId) {
        return subscriptionRepository.findByUserId(userId);
    }
    public OfferResponseDTO createOffer (OfferRequestDTO offerRequestDTO){
        if (offerRepository.existsByName(offerRequestDTO.getName())){

            throw new BundleAlreadyExistsException("A Offer with this name already exists"
                    + offerRequestDTO.getName());
        }

        Offer newOffer = offerRepository.save(
                OfferMapper.toModel(offerRequestDTO));

//        billingServiceGrpcClient.createBillingAccount(newBundle.getId().toString(),
//                newBundle.getName(), newBundle.getDescription());
//
//        kafkaProducer.sentEvent(newBundle);

        return OfferMapper.toDTO(newOffer);
    }

    public OfferResponseDTO updateOffer(Integer id, OfferRequestDTO offerRequestDTO){
        Offer offer = offerRepository.findById(id)
                .orElseThrow(()-> new BundleNotFindException("Offer not find with ID:" +id));

        if (offerRepository.existsByNameAndIdNot(offerRequestDTO.getName(), id)){
            throw new BundleAlreadyExistsException("A Offer with this name already exists"
                    + offerRequestDTO.getName());
        }

        offer.setName(offerRequestDTO.getName());
        offer.setDescription(offerRequestDTO.getDescription());

        Offer updatedOffer = offerRepository.save(offer);

        return OfferMapper.toDTO(updatedOffer);
    }

    public void deleteOffer(Integer id){
        offerRepository.deleteById(id);
    }

}


