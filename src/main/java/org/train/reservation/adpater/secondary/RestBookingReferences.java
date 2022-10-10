package org.train.reservation.adpater.secondary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.train.reservation.domain.port.BookingReferences;

@Component
public class RestBookingReferences implements BookingReferences {

    @Autowired
    private RestTemplate bookingReferenceClient;

    @Override
    public String retrieveBookingReference() {
        return bookingReferenceClient.getForObject("/booking_reference", String.class);
    }
}
