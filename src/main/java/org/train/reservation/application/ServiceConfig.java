package org.train.reservation.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.train.reservation.domain.service.TicketOffice;
import org.train.reservation.domain.port.BookingReferences;
import org.train.reservation.domain.port.TrainDataGateway;

@Configuration
public class ServiceConfig {

    @Bean
    public TicketOffice ticketOffice(BookingReferences bookingReferences, TrainDataGateway trainDataGateway) {
        return new TicketOffice(bookingReferences, trainDataGateway);
    }

}
