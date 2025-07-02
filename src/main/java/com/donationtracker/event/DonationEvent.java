package com.donationtracker.event;

import com.donationtracker.model.DonationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationEvent {
    private String eventId;
    private String donorId;
    private String campaignId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private LocalDateTime timestamp;
    private DonationStatus status;
    private String transactionId;
    private String eventType; // CREATED, UPDATED, COMPLETED, FAILED
    private String metadata; // Additional JSON metadata if needed
} 