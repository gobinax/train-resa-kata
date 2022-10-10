package org.train.reservation.domain.service;

import org.train.reservation.domain.model.Coach;
import org.train.reservation.domain.model.Seat;
import org.train.reservation.domain.model.Train;
import org.train.reservation.domain.port.BookingReferences;
import org.train.reservation.domain.port.TrainDataGateway;

import java.util.List;

public class TicketOffice {

    private static final double MAX_TRAIN_OCCUPANCY_RATE = .7;

    private final BookingReferences bookingReferences;
    private final TrainDataGateway trainDataGateway;

    public TicketOffice(BookingReferences bookingReferences, TrainDataGateway trainDataGateway) {
        this.bookingReferences = bookingReferences;
        this.trainDataGateway = trainDataGateway;
    }

    public Reservation makeReservation(ReservationRequest request) {
        String bookingId = bookingReferences.retrieveBookingReference();
        Train train = trainDataGateway.getTrainData(request.trainId);

        double trainOccupancy = computeFutureTrainOccupancy(train, request.seatCount);
        if (trainOccupancy > MAX_TRAIN_OCCUPANCY_RATE) {
            return new Reservation(request.trainId, List.of(), "");
        }

        for (Coach coach : train.getCoaches()) {
            int occupiedSeatCount = coach.getTotalSeatCount() - coach.getAvailableSeats().size();
            double coachOccupancy = (double) (request.seatCount + occupiedSeatCount) / coach.getTotalSeatCount();
            Reservation bookedSeats = doMakeReservation(request, bookingId, coach, coachOccupancy);
            if (bookedSeats != null) return bookedSeats;
        }
        for (Coach coach : train.getCoaches()) {
            int occupiedSeatCount = coach.getTotalSeatCount() - coach.getAvailableSeats().size();
            double coachOccupancy = (double) (occupiedSeatCount) / coach.getTotalSeatCount();
            Reservation bookedSeats = doMakeReservation(request, bookingId, coach, coachOccupancy);
            if (bookedSeats != null) return bookedSeats;
        }

        return new Reservation(request.trainId, List.of(), "");
    }

    private Reservation doMakeReservation(ReservationRequest request, String bookingId, Coach coach, double coachOccupancy) {
        if (coachOccupancy <= MAX_TRAIN_OCCUPANCY_RATE) {
            List<Seat> bookedSeats = coach.getAvailableSeats().stream()
                    .limit(request.seatCount)
                    .map(n -> new Seat(coach.getCoach(), n))
                    .toList();
            trainDataGateway.reserve(request.trainId, bookedSeats, bookingId);
            return new Reservation(request.trainId, bookedSeats, bookingId);
        }
        return null;
    }

    private double computeFutureTrainOccupancy(Train train, int seatCount) {
        int totalCount = 0;
        int availableCount = 0;
        for (Coach coach : train.getCoaches()) {
            totalCount += coach.getTotalSeatCount();
            availableCount += coach.getAvailableSeats().size();
        }
        return (double) (totalCount - availableCount + seatCount) / totalCount;
    }
}