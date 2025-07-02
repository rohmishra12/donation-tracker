package com.donationtracker.kafka;

import com.donationtracker.event.DonationEvent;
import com.donationtracker.service.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DonationEventConsumer {

    private final DonationService donationService;

    @KafkaListener(
        topics = "${kafka.topics.donation-events}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeDonationEvents(List<DonationEvent> events, Acknowledgment ack) {
        log.info("Received batch of {} donation events", events.size());
        
        try {
            for (DonationEvent event : events) {
                try {
                    donationService.processDonation(event)
                        .whenComplete((donation, error) -> {
                            if (error != null) {
                                log.error("Error processing donation event: {}", event.getEventId(), error);
                                donationService.handleFailedDonation(event);
                            } else {
                                log.info("Successfully processed donation: {}", donation.getId());
                            }
                        });
                } catch (Exception e) {
                    log.error("Failed to process donation event: {}", event.getEventId(), e);
                    donationService.handleFailedDonation(event);
                }
            }
        } finally {
            ack.acknowledge();
        }
    }
} 