package org.train.reservation.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BookingReferenceGateway {

    @Autowired
    private RestTemplate bookingReferenceClient;

    public String retrieveBookingReference() {
        return bookingReferenceClient.getForObject("/booking_reference", String.class);
    }
}
