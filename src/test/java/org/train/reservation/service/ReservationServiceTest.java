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
import static org.mockito.ArgumentMatchers.any;

class ReservationServiceTest {

    BookingReferenceGateway bookingReferences;
    TrainDataGateway trainDataGateway;

    ReservationService reservationService;

    @Test
    public void should_NOT_book_seats_if_train_above_70_percent_occupancy() {
        // given
        String bookingId = "75bcd15";

        String trainId = "express_2000";
        Map<String, Seat> seats = new HashMap<>();
        // 8/10 occupancy
        seats.put("1A", createOccupiedSeat(1, "A", "75bcd13"));
        seats.put("2A", createOccupiedSeat(2, "A", "75bcd13"));
        seats.put("3A", createOccupiedSeat(3, "A", "75bcd13"));
        seats.put("4A", createOccupiedSeat(4, "A", "75bcd13"));
        seats.put("5A", createOccupiedSeat(5, "A", "75bcd13"));
        seats.put("6A", createOccupiedSeat(6, "A", "75bcd13"));
        seats.put("7A", createOccupiedSeat(7, "A", "75bcd13"));
        seats.put("8A", createOccupiedSeat(8, "A", "75bcd13"));
        seats.put("9A", createOccupiedSeat(9, "A", ""));
        seats.put("10A", createOccupiedSeat(10, "A", ""));
        // 6/10 occupancy
        seats.put("1B", createOccupiedSeat(1, "B", "75bcd14"));
        seats.put("2B", createOccupiedSeat(2, "B", "75bcd14"));
        seats.put("3B", createOccupiedSeat(3, "B", "75bcd14"));
        seats.put("4B", createOccupiedSeat(4, "B", "75bcd14"));
        seats.put("5B", createOccupiedSeat(5, "B", "75bcd14"));
        seats.put("6B", createOccupiedSeat(6, "B", "75bcd14"));
        seats.put("7B", createOccupiedSeat(7, "B", ""));
        seats.put("8B", createOccupiedSeat(8, "B", ""));
        seats.put("9B", createOccupiedSeat(9, "B", ""));
        seats.put("10B", createOccupiedSeat(10, "B", ""));

        Train train = new Train();
        train.setSeats(seats);

        // when
        Reservation reservation = reservationService.makeReservation(new ReservationRequest(trainId, 1));

        //then
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo("");
        assertThat(reservation.seats).isEmpty();
    }

    private Seat createOccupiedSeat(int seatNumber, String coach, String bookingReference) {
        Seat seat = new Seat();
        seat.setSeat_number(seatNumber);
        seat.setCoach(coach);
        seat.setBooking_reference(bookingReference);
        return seat;
    }
}