package org.train.reservation.infra;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.train.reservation.domain.port.BookingReferenceGateway;

@Component
public class RestBookingReferenceGateway implements BookingReferenceGateway {

    private final RestTemplate bookingReferenceClient;

    public RestBookingReferenceGateway(RestTemplate bookingReferenceClient) {
        this.bookingReferenceClient = bookingReferenceClient;
    }

    @Override
    public String retrieveBookingReference() {
        return bookingReferenceClient.getForObject("/booking_reference", String.class);
    }
}
