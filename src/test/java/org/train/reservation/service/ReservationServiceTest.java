package org.train.reservation.service;

import org.junit.jupiter.api.Test;
import org.train.reservation.gateway.BookingReferenceGateway;
import org.train.reservation.gateway.TrainDataGateway;
import org.train.reservation.model.Coach;
import org.train.reservation.model.Train;
import org.train.reservation.pojo.Reservation;
import org.train.reservation.pojo.ReservationRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    BookingReferenceGateway bookingReferences = mock(BookingReferenceGateway.class);
    TrainDataGateway trainDataGateway = mock(TrainDataGateway.class);

    ReservationService reservationService = new ReservationService(bookingReferences, trainDataGateway);

    @Test
    public void should_NOT_book_seats_if_train_above_70_percent_occupancy() {
        // given
        String bookingId = "75bcd15";
        when(bookingReferences.retrieveBookingReference()).thenReturn(bookingId);

        String trainId = "galaxy_express_999";
        Train train = new Train(
                // 8/10 occupancy
                new Coach("A", 10, List.of(1, 2)),
                // 6/10 occupancy
                new Coach("B", 10, List.of(1, 2, 3, 4))
        ); // => overall 70% occupancy

        when(trainDataGateway.getTrainData(trainId)).thenReturn(train);


        // when
        Reservation reservation = reservationService.makeReservation(new ReservationRequest(trainId, 1));

        //then
        verify(trainDataGateway, never()).reserve(any(), any(), any());
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo("");
        assertThat(reservation.seats).isEqualTo(List.of());
    }

    @Test
    public void should_book_seats_if_train_under_70_occupancy_and_prefer_coach_under_70_percent_occupancy() {
        // given
        String bookingId = "75bcd15";
        when(bookingReferences.retrieveBookingReference()).thenReturn(bookingId);

        String trainId = "galaxy_express_999";
        Train train = new Train(
                // 8/10 occupancy
                new Coach("A", 10, List.of(1, 2)),
                // 6/10 occupancy
                new Coach("B", 10, List.of(1, 2, 3, 4)),
                // 5/10 occupancy
                new Coach("C", 10, List.of(1, 2, 3, 4, 5))
        );
        when(trainDataGateway.getTrainData(trainId)).thenReturn(train);

        // when
        Reservation reservation = reservationService.makeReservation(new ReservationRequest(trainId, 2));

        //then
        // => book 2 => train overall 21/30 (70%)
        //           => coach C 7/10 occupancy (remains under 70%)
        List<String> requestedSeats = List.of("1C","2C");
        verify(trainDataGateway).reserve(trainId, requestedSeats, bookingId);
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(bookingId);
        assertThat(reservation.seats).containsExactlyInAnyOrder("1C", "2C");
    }

    @Test
    public void should_overbook_coach_over_70_to_reserve_seats_in_same_coach_when_train_occupancy_stays_under_70() {
        // given
        String bookingId = "75bcd15";
        when(bookingReferences.retrieveBookingReference()).thenReturn(bookingId);

        String trainId = "galaxy_express_999";

        Train train = new Train(
                // 6/10 seats available
                new Coach("A", 10, List.of(1, 2, 3, 4)),
                // 6/10 seats available
                new Coach("B", 10, List.of(1, 2, 3, 4))
        );

        when(trainDataGateway.getTrainData(trainId)).thenReturn(train);

        // when
        Reservation reservation = reservationService.makeReservation(new ReservationRequest(trainId, 2));

        //then
        // => book 2 => book 2 in coach A => overall 70%
        List<String> requestedSeats = List.of("1A","2A");
        verify(trainDataGateway).reserve(trainId, requestedSeats, bookingId);
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(bookingId);
        assertThat(reservation.seats).containsExactlyInAnyOrder("1A","2A");
    }
}