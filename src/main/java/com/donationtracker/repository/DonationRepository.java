package com.donationtracker.repository;

import com.donationtracker.model.Donation;
import com.donationtracker.model.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByCampaignId(String campaignId);
    List<Donation> findByDonorId(String donorId);
    List<Donation> findByStatus(DonationStatus status);
    Optional<Donation> findByTransactionId(String transactionId);
    
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.campaignId = ?1 AND d.status = ?2")
    BigDecimal sumAmountByCampaignAndStatus(String campaignId, DonationStatus status);
    
    @Query("SELECT COUNT(d) FROM Donation d WHERE d.campaignId = ?1 AND d.timestamp BETWEEN ?2 AND ?3")
    Long countDonationsByCampaignAndTimeRange(String campaignId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT d FROM Donation d WHERE d.timestamp >= ?1 AND d.status = ?2")
    List<Donation> findRecentDonationsByStatus(LocalDateTime since, DonationStatus status);
} 