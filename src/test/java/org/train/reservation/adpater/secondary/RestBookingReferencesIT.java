package org.train.reservation.adpater.secondary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestBookingReferencesIT {

    @Autowired
    RestBookingReferences bookingReferences;

    @Test
    void should_retrieve_booking_reference() {
        String bookingReference = bookingReferences.retrieveBookingReference();
        Assertions.assertThat(bookingReference).isNotBlank();
    }
}