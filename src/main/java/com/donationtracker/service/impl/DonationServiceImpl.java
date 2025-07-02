package com.donationtracker.service.impl;

import com.donationtracker.model.Donation;
import com.donationtracker.model.DonationStatus;
import com.donationtracker.event.DonationEvent;
import com.donationtracker.repository.DonationRepository;
import com.donationtracker.service.DonationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Slf4j
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final KafkaTemplate<String, DonationEvent> kafkaTemplate;
    private final Executor asyncExecutor;

    @Override
    @Transactional
    @CircuitBreaker(name = "donationService", fallbackMethod = "processDonationFallback")
    @Retry(name = "donationService")
    public CompletableFuture<Donation> processDonation(DonationEvent event) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Processing donation event: {}", event.getEventId());
                
                Donation donation = new Donation();
                donation.setDonorId(event.getDonorId());
                donation.setCampaignId(event.getCampaignId());
                donation.setAmount(event.getAmount());
                donation.setCurrency(event.getCurrency());
                donation.setPaymentMethod(event.getPaymentMethod());
                donation.setStatus(DonationStatus.PROCESSING);
                donation.setTransactionId(event.getTransactionId());
                
                Donation savedDonation = donationRepository.save(donation);
                
                // Publish analytics event
                event.setStatus(DonationStatus.PROCESSING);
                kafkaTemplate.send("donation-analytics", event.getEventId(), event);
                
                return savedDonation;
            } catch (Exception e) {
                log.error("Error processing donation: {}", event.getEventId(), e);
                throw new RuntimeException("Failed to process donation", e);
            }
        }, asyncExecutor);
    }

    @Override
    @CircuitBreaker(name = "donationService")
    public List<Donation> getDonationsByCampaign(String campaignId) {
        return donationRepository.findByCampaignId(campaignId);
    }

    @Override
    @CircuitBreaker(name = "donationService")
    public List<Donation> getDonationsByDonor(String donorId) {
        return donationRepository.findByDonorId(donorId);
    }

    @Override
    @CircuitBreaker(name = "donationService")
    public Donation getDonationById(Long id) {
        return donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found: " + id));
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "donationService")
    public void updateDonationStatus(Long id, String status) {
        Donation donation = getDonationById(id);
        donation.setStatus(DonationStatus.valueOf(status));
        donationRepository.save(donation);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "donationService")
    public void handleFailedDonation(DonationEvent event) {
        log.warn("Handling failed donation: {}", event.getEventId());
        Donation donation = donationRepository.findByTransactionId(event.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Donation not found for transaction: " + event.getTransactionId()));
        
        donation.setStatus(DonationStatus.FAILED);
        donationRepository.save(donation);
        
        // Publish failure event for analytics
        event.setStatus(DonationStatus.FAILED);
        kafkaTemplate.send("donation-analytics", event.getEventId(), event);
    }

    // Fallback method for circuit breaker
    @SuppressWarnings("unused")
    private CompletableFuture<Donation> processDonationFallback(DonationEvent event, Exception e) {
        log.error("Circuit breaker fallback triggered for donation: {}", event.getEventId(), e);
        // Store failed donation in a separate table or queue for retry
        return CompletableFuture.failedFuture(e);
    }
} 