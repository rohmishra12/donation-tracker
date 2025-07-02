package com.donationtracker.service;

import com.donationtracker.model.Donation;
import com.donationtracker.event.DonationEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DonationService {
    CompletableFuture<Donation> processDonation(DonationEvent event);
    List<Donation> getDonationsByCampaign(String campaignId);
    List<Donation> getDonationsByDonor(String donorId);
    Donation getDonationById(Long id);
    void updateDonationStatus(Long id, String status);
    void handleFailedDonation(DonationEvent event);
} 