package org.train.reservation.application;

import org.springframework.stereotype.Component;
import org.train.reservation.domain.model.Seat;
import org.train.reservation.domain.model.Train;
import org.train.reservation.domain.port.BookingReferenceGateway;
import org.train.reservation.domain.port.TrainDataGateway;
import org.train.reservation.domain.service.Reservation;

import java.util.List;

@Component
public class ReservationService {

    private final BookingReferenceGateway bookingReferenceGateway;
    private final TrainDataGateway trainDataGateway;

    public ReservationService(BookingReferenceGateway bookingReferenceGateway, TrainDataGateway trainDataGateway) {
        this.bookingReferenceGateway = bookingReferenceGateway;
        this.trainDataGateway = trainDataGateway;
    }

    public Reservation makeReservation(ReservationRequest request) {
        Train train = trainDataGateway.getTrainData(request.trainId);
        List<Seat> bookedSeats = train.reserve(request.seatCount);
        if (bookedSeats.isEmpty()) {
            return new Reservation(request.trainId, List.of(), "");
        }
        String bookingId = bookingReferenceGateway.retrieveBookingReference();
        trainDataGateway.reserve(request.trainId, bookedSeats, bookingId);
        return new Reservation(request.trainId, bookedSeats, bookingId);
    }
}