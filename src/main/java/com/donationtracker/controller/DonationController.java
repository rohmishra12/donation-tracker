package com.donationtracker.controller;

import com.donationtracker.model.Donation;
import com.donationtracker.event.DonationEvent;
import com.donationtracker.service.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Slf4j
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    public CompletableFuture<ResponseEntity<Donation>> createDonation(@RequestBody DonationEvent event) {
        log.info("Received donation request for campaign: {}", event.getCampaignId());
        return donationService.processDonation(event)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    log.error("Error processing donation", ex);
                    return ResponseEntity.internalServerError().build();
                });
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<Donation>> getDonationsByCampaign(@PathVariable String campaignId) {
        return ResponseEntity.ok(donationService.getDonationsByCampaign(campaignId));
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<Donation>> getDonationsByDonor(@PathVariable String donorId) {
        return ResponseEntity.ok(donationService.getDonationsByDonor(donorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donation> getDonation(@PathVariable Long id) {
        return ResponseEntity.ok(donationService.getDonationById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateDonationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        donationService.updateDonationStatus(id, status);
        return ResponseEntity.ok().build();
    }
} 