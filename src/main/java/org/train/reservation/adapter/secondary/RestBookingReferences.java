package org.train.reservation.adapter.secondary;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.train.reservation.domain.port.BookingReferences;

@Component
public class RestBookingReferences implements BookingReferences {

    private final RestTemplate bookingReferenceClient;

    public RestBookingReferences(RestTemplate bookingReferenceClient) {
        this.bookingReferenceClient = bookingReferenceClient;
    }

    @Override
    public String retrieveBookingReference() {
        return bookingReferenceClient.getForObject("/booking_reference", String.class);
    }
}
