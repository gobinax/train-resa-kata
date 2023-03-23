package org.train.reservation.exposition;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.train.reservation.domain.service.Reservation;
import org.train.reservation.application.ReservationRequest;
import org.train.reservation.application.ReservationService;

@Controller
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    public Reservation reserve(ReservationRequestDto request) {
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
            return new ReservationRequest(train_id(), seat_count());
        }

        public String train_id() {
            return train_id;
        }

        public Integer seat_count() {
            return seat_count;
        }

        @Override
        public String toString() {
            return "ReservationRequestDto[" +
                    "train_id=" + train_id + ", " +
                    "seat_count=" + seat_count + ']';
        }

    }
}
