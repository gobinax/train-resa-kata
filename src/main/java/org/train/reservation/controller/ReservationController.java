package org.train.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.train.reservation.pojo.Reservation;
import org.train.reservation.pojo.ReservationRequest;
import org.train.reservation.service.ReservationService;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping(value = "/reserve", consumes = {"application/json"})
    public Reservation reserve(@RequestBody ReservationRequestDto request) {
        ReservationRequest request1 = request.mapToRequest();
        return reservationService.makeReservation(request1);
    }

    static final class ReservationRequestDto {
        private final String train_id;
        private final Integer seat_count;

        ReservationRequestDto(String train_id, Integer seat_count) {
            this.train_id = train_id;
            this.seat_count = seat_count;
        }

        public ReservationRequest mapToRequest() {
            return new ReservationRequest(train_id, seat_count);
        }

        @Override
        public String toString() {
            return "ReservationRequestDto[" +
                    "train_id=" + train_id + ", " +
                    "seat_count=" + seat_count + ']';
        }

    }
}
