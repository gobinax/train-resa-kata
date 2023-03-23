package org.train.reservation.service;

import org.junit.jupiter.api.Test;
import org.train.reservation.gateway.BookingReferenceGateway;
import org.train.reservation.gateway.TrainDataGateway;
import org.train.reservation.pojo.Reservation;
import org.train.reservation.pojo.ReservationRequest;
import org.train.reservation.pojo.Seat;
import org.train.reservation.pojo.Train;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String trainId = "express_2000";
        Map<String, Seat> seats = new HashMap<>();
        // 8/10 occupancy
        seats.put("1A", createSeat(1, "A", "75bcd14"));
        seats.put("2A", createSeat(2, "A", "75bcd14"));
        seats.put("3A", createSeat(3, "A", "75bcd14"));
        seats.put("4A", createSeat(4, "A", "75bcd14"));
        seats.put("5A", createSeat(5, "A", "75bcd14"));
        seats.put("6A", createSeat(6, "A", "75bcd14"));
        seats.put("7A", createSeat(7, "A", "75bcd14"));
        seats.put("8A", createSeat(8, "A", "75bcd14"));
        seats.put("9A", createSeat(9, "A", ""));
        seats.put("10A", createSeat(10, "A", ""));
        // 6/10 occupancy
        seats.put("1B", createSeat(1, "B", "75bcd13"));
        seats.put("2B", createSeat(2, "B", "75bcd13"));
        seats.put("3B", createSeat(3, "B", "75bcd13"));
        seats.put("4B", createSeat(4, "B", "75bcd13"));
        seats.put("5B", createSeat(5, "B", "75bcd13"));
        seats.put("6B", createSeat(6, "B", "75bcd13"));
        seats.put("7B", createSeat(7, "B", ""));
        seats.put("8B", createSeat(8, "B", ""));
        seats.put("9B", createSeat(9, "B", ""));
        seats.put("10B", createSeat(10, "B", ""));

        Train train = new Train();
        train.setSeats(seats);
        when(trainDataGateway.getTrainData(trainId)).thenReturn(train);

        // when
        Reservation reservation = reservationService.makeReservation(new ReservationRequest(trainId, 1));

        //then
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo("");
        assertThat(reservation.seats).isEmpty();
    }

    @Test
    public void should_book_seats_if_train_under_70_occupancy_and_prefer_coach_under_70_percent_occupancy() {
        // given
        String bookingId = "75bcd15";
        when(bookingReferences.retrieveBookingReference()).thenReturn(bookingId);

        String trainId = "express_2000";
        Map<String, Seat> seats = new HashMap<>();
        // 8/10 occupancy
        seats.put("1A", createSeat(1, "A", "75bcd14"));
        seats.put("2A", createSeat(2, "A", "75bcd14"));
        seats.put("3A", createSeat(3, "A", "75bcd14"));
        seats.put("4A", createSeat(4, "A", "75bcd14"));
        seats.put("5A", createSeat(5, "A", "75bcd14"));
        seats.put("6A", createSeat(6, "A", "75bcd14"));
        seats.put("7A", createSeat(7, "A", "75bcd14"));
        seats.put("8A", createSeat(8, "A", "75bcd14"));
        seats.put("9A", createSeat(9, "A", ""));
        seats.put("10A", createSeat(10, "A", ""));
        // 6/10 occupancy
        seats.put("1B", createSeat(1, "B", "75bcd13"));
        seats.put("2B", createSeat(2, "B", "75bcd13"));
        seats.put("3B", createSeat(3, "B", "75bcd13"));
        seats.put("4B", createSeat(4, "B", "75bcd13"));
        seats.put("5B", createSeat(5, "B", "75bcd13"));
        seats.put("6B", createSeat(6, "B", "75bcd13"));
        seats.put("7B", createSeat(7, "B", ""));
        seats.put("8B", createSeat(8, "B", ""));
        seats.put("9B", createSeat(9, "B", ""));
        seats.put("10B", createSeat(10, "B", ""));
        // 5/10 occupancy
        seats.put("1C", createSeat(1, "C", "75bcd12"));
        seats.put("2C", createSeat(2, "C", "75bcd12"));
        seats.put("3C", createSeat(3, "C", "75bcd12"));
        seats.put("4C", createSeat(4, "C", "75bcd12"));
        seats.put("5C", createSeat(5, "C", "75bcd12"));
        seats.put("6C", createSeat(6, "C", ""));
        seats.put("7C", createSeat(7, "C", ""));
        seats.put("8C", createSeat(8, "C", ""));
        seats.put("9C", createSeat(9, "C", ""));
        seats.put("10C", createSeat(10, "C", ""));

        Train train = new Train();
        train.setSeats(seats);
        when(trainDataGateway.getTrainData(trainId)).thenReturn(train);

        // when
        Reservation reservation = reservationService.makeReservation(new ReservationRequest(trainId, 2));

        //then
        // => book 2 => train overall 21/30 (70%)
        //           => coach C 7/10 occupancy (remains under 70%)
        verify(trainDataGateway).reserve(eq(trainId), any(), eq(bookingId));
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(bookingId);
        assertThat(reservation.seats).hasSize(2);
        assertThat(reservation.seats).allMatch(seat -> List.of("7C", "8C", "9C", "10C").contains(seat));
    }

    @Test
    public void should_overbook_coach_over_70_to_reserve_seats_in_same_coach_when_train_occupancy_stays_under_70() {
        // given
        String bookingId = "75bcd15";
        when(bookingReferences.retrieveBookingReference()).thenReturn(bookingId);

        String trainId = "express_2000";
        Map<String, Seat> seats = new HashMap<>();
        // 6/10 occupancy
        seats.put("1A", createSeat(1, "A", "75bcd14"));
        seats.put("2A", createSeat(2, "A", "75bcd14"));
        seats.put("3A", createSeat(3, "A", "75bcd14"));
        seats.put("4A", createSeat(4, "A", "75bcd14"));
        seats.put("5A", createSeat(5, "A", "75bcd14"));
        seats.put("6A", createSeat(6, "A", "75bcd14"));
        seats.put("7A", createSeat(7, "A", ""));
        seats.put("8A", createSeat(8, "A", ""));
        seats.put("9A", createSeat(9, "A", ""));
        seats.put("10A", createSeat(10, "A", ""));
        // 6/10 occupancy
        seats.put("1B", createSeat(1, "B", "75bcd13"));
        seats.put("2B", createSeat(2, "B", "75bcd13"));
        seats.put("3B", createSeat(3, "B", "75bcd13"));
        seats.put("4B", createSeat(4, "B", "75bcd13"));
        seats.put("5B", createSeat(5, "B", "75bcd13"));
        seats.put("6B", createSeat(6, "B", "75bcd13"));
        seats.put("7B", createSeat(7, "B", ""));
        seats.put("8B", createSeat(8, "B", ""));
        seats.put("9B", createSeat(9, "B", ""));
        seats.put("10B", createSeat(10, "B", ""));

        Train train = new Train();
        train.setSeats(seats);
        when(trainDataGateway.getTrainData(trainId)).thenReturn(train);

        // when
        Reservation reservation = reservationService.makeReservation(new ReservationRequest(trainId, 2));

        //then
        // => book 2 => book 2 in coach A => overall 70%
        verify(trainDataGateway).reserve(eq(trainId), any(), eq(bookingId));
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(bookingId);
        assertThat(reservation.seats).hasSize(2);
        assertThat(reservation.seats).allMatch(seat -> List.of("7A", "8A", "9A", "10A").contains(seat));
    }

    private Seat createSeat(int seatNumber, String coach, String bookingReference) {
        Seat seat = new Seat();
        seat.setSeat_number(seatNumber);
        seat.setCoach(coach);
        seat.setBooking_reference(bookingReference);
        return seat;
    }
}