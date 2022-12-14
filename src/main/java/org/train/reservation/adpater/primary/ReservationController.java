package org.train.reservation.adpater.primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.train.reservation.domain.service.Reservation;
import org.train.reservation.domain.service.ReservationRequest;
import org.train.reservation.domain.service.TicketOffice;

@Controller
public class ReservationController {

    @Autowired
    private TicketOffice ticketOffice;

    @PostMapping("/reserve")
    public Reservation reserve(ReservationRequestDto request) {
        ReservationRequest request1 = request.mapToRequest();
        return ticketOffice.makeReservation(request1);
    }

    record ReservationRequestDto(String train_id, Integer seat_count) {
        public ReservationRequest mapToRequest() {
            return new ReservationRequest(train_id(), seat_count());
        }
    }
}
