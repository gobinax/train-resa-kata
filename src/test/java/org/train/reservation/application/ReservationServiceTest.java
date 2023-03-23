package org.train.reservation.application;

import org.junit.jupiter.api.Test;
import org.train.reservation.domain.model.Coach;
import org.train.reservation.domain.model.Seat;
import org.train.reservation.domain.model.Train;
import org.train.reservation.domain.port.BookingReferenceGateway;
import org.train.reservation.domain.port.TrainDataGateway;
import org.train.reservation.domain.service.Reservation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Test
    public void should_call_reservation_if_seats_available() {
        // given
        String bookingId = "75bcd15";
        BookingReferenceGateway bookingReferenceGateway = mock(BookingReferenceGateway.class);
        when(bookingReferenceGateway.retrieveBookingReference()).thenReturn(bookingId);

        String trainId = "galaxy_express_999";
        TrainDataGateway trainDataGateway = mock(TrainDataGateway.class);
        when(trainDataGateway.getTrainData(trainId)).thenReturn(new Train(List.of(
                new Coach("A", 5, List.of(1, 2, 3, 4, 5))
        )));

        // when
        Reservation reservation = new ReservationService(bookingReferenceGateway, trainDataGateway)
                .makeReservation(new ReservationRequest(trainId, 2));

        //then
        List<Seat> requestedSeats = List.of(new Seat("A", 1), new Seat("A", 2));
        verify(trainDataGateway).reserve(trainId, requestedSeats, bookingId);
        assertThat(reservation).isEqualTo(new Reservation(trainId, requestedSeats, bookingId));
    }

    @Test
    public void should_NOT_call_reservation_if_no_seats_available() {
        // given
        String trainId = "galaxy_express_999";
        TrainDataGateway trainDataGateway = mock(TrainDataGateway.class);
        when(trainDataGateway.getTrainData(trainId)).thenReturn(new Train(List.of(
                new Coach("A", 5, List.of())
        )));

        // when
        Reservation reservation = new ReservationService(mock(BookingReferenceGateway.class), trainDataGateway)
                .makeReservation(new ReservationRequest(trainId, 2));

        //then
        verify(trainDataGateway, never()).reserve(any(), any(), any());
        assertThat(reservation).isEqualTo(new Reservation(trainId, List.of(), ""));
    }
}
