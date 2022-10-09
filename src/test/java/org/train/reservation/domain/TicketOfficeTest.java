package org.train.reservation.domain;

import org.junit.jupiter.api.Test;
import org.train.reservation.domain.model.Coach;
import org.train.reservation.domain.model.Seat;
import org.train.reservation.domain.model.Train;
import org.train.reservation.domain.port.BookingReferences;
import org.train.reservation.domain.port.TrainDataGateway;
import org.train.reservation.domain.service.Reservation;
import org.train.reservation.domain.service.ReservationRequest;
import org.train.reservation.domain.service.TicketOffice;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TicketOfficeTest {

    @Test
    public void should_NOT_book_seats_if_train_above_70_percent_occupancy() {
        // given
        String bookingId = "75bcd15";
        BookingReferences bookingReferences = mock(BookingReferences.class);
        when(bookingReferences.provideBookingReference()).thenReturn(bookingId);

        String trainId = "galaxy_express_999";
        Train train = new Train(
                // 8/10 occupancy
                new Coach("A", 10, List.of(1, 2)),
                // 6/10 occupancy
                new Coach("B", 10, List.of(1, 2, 3, 4))
        ); // => overall 70% occupancy

        TrainDataGateway trainDataGateway = mock(TrainDataGateway.class);
        when(trainDataGateway.getTrainData(trainId)).thenReturn(train);

        TicketOffice ticketOffice = new TicketOffice(bookingReferences, trainDataGateway);

        // when
        Reservation reservation = ticketOffice.makeReservation(new ReservationRequest(trainId, 1));

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
        BookingReferences bookingReferences = mock(BookingReferences.class);
        when(bookingReferences.provideBookingReference()).thenReturn(bookingId);

        String trainId = "galaxy_express_999";
        Train train = new Train(
                // 8/10 occupancy
                new Coach("A", 10, List.of(1, 2)),
                // 6/10 occupancy
                new Coach("B", 10, List.of(1, 2, 3, 4)),
                // 5/10 occupancy
                new Coach("C", 10, List.of(1, 2, 3, 4, 5))
        );
        TrainDataGateway trainDataGateway = mock(TrainDataGateway.class);
        when(trainDataGateway.getTrainData(trainId)).thenReturn(train);

        TicketOffice ticketOffice = new TicketOffice(bookingReferences, trainDataGateway);

        // when
        Reservation reservation = ticketOffice.makeReservation(new ReservationRequest(trainId, 2));

        //then
        // => book 2 => train overall 21/30 (70%)
        //           => coach C 7/10 occupancy (remains under 70%)
        List<Seat> requestedSeats = List.of(new Seat("C", 1), new Seat("C", 2));
        verify(trainDataGateway).reserve(trainId, requestedSeats, bookingId);
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(bookingId);
        assertThat(reservation.seats).isEqualTo(requestedSeats);
    }

    @Test
    public void should_overbook_coach_over_70_to_reserve_seats_in_same_coach_when_train_occupancy_stays_under_70() {
        // given
        String bookingId = "75bcd15";
        BookingReferences bookingReferences = mock(BookingReferences.class);
        when(bookingReferences.provideBookingReference()).thenReturn(bookingId);

        String trainId = "galaxy_express_999";

        Train train = new Train(
                // 6/10 seats available
                new Coach("A", 10, List.of(1, 2, 3, 4)),
                // 6/10 seats available
                new Coach("B", 10, List.of(1, 2, 3, 4))
        );

        TrainDataGateway trainDataGateway = mock(TrainDataGateway.class);
        when(trainDataGateway.getTrainData(trainId)).thenReturn(train);

        TicketOffice ticketOffice = new TicketOffice(bookingReferences, trainDataGateway);

        // when
        Reservation reservation = ticketOffice.makeReservation(new ReservationRequest(trainId, 2));

        //then
        // => book 2 => book 2 in coach A => overall 70%
        List<Seat> requestedSeats = List.of(
                new Seat("A", 1),
                new Seat("A", 2));
        verify(trainDataGateway).reserve(trainId, requestedSeats, bookingId);
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(bookingId);
        assertThat(reservation.seats).isEqualTo(requestedSeats);
    }
}
