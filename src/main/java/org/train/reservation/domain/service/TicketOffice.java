package org.train.reservation.domain.service;

import org.train.reservation.domain.model.Seat;
import org.train.reservation.domain.model.Train;
import org.train.reservation.domain.port.BookingReferences;
import org.train.reservation.domain.port.TrainDataGateway;

import java.util.List;

public class TicketOffice {

    private final BookingReferences bookingReferences;
    private final TrainDataGateway trainDataGateway;

    public TicketOffice(BookingReferences bookingReferences, TrainDataGateway trainDataGateway) {
        this.bookingReferences = bookingReferences;
        this.trainDataGateway = trainDataGateway;
    }

    public Reservation makeReservation(ReservationRequest request) {
        Train train = trainDataGateway.getTrainData(request.trainId);
        List<Seat> bookedSeats = train.reserve(request.seatCount);
        if(bookedSeats.isEmpty()) {
            return new Reservation(request.trainId, List.of(), "");
        }
        String bookingId = bookingReferences.retrieveBookingReference();
        trainDataGateway.reserve(request.trainId, bookedSeats, bookingId);
        return new Reservation(request.trainId, bookedSeats, bookingId);
    }
}