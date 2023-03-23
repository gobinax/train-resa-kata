package org.train.reservation.service;

import org.springframework.stereotype.Component;
import org.train.reservation.gateway.BookingReferenceGateway;
import org.train.reservation.gateway.TrainDataGateway;
import org.train.reservation.model.Coach;
import org.train.reservation.model.Train;
import org.train.reservation.pojo.Reservation;
import org.train.reservation.pojo.ReservationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationService {

    private static final double MAX_TRAIN_OCCUPANCY_RATE = .7;

    private final BookingReferenceGateway bookingReferenceGateway;
    private final TrainDataGateway trainDataGateway;

    public ReservationService(BookingReferenceGateway bookingReferenceGateway, TrainDataGateway trainDataGateway) {
        this.bookingReferenceGateway = bookingReferenceGateway;
        this.trainDataGateway = trainDataGateway;
    }

    public Reservation makeReservation(ReservationRequest request) {
        String bookingId = bookingReferenceGateway.retrieveBookingReference();
        Train train = trainDataGateway.getTrainData(request.getTrainId());

        double trainOccupancy = computeFutureTrainOccupancy(train, request.getSeatCount());
        if (trainOccupancy > MAX_TRAIN_OCCUPANCY_RATE) {
            return new Reservation(request.getTrainId(), List.of(), "");
        }

        for (Coach coach : train.getCoaches()) {
            int occupiedSeatCount = coach.getTotalSeatCount() - coach.getAvailableSeats().size();
            double coachOccupancy = (double) (request.getSeatCount() + occupiedSeatCount) / coach.getTotalSeatCount();
            Reservation bookedSeats = doMakeReservation(request, bookingId, coach, coachOccupancy);
            if (bookedSeats != null) return bookedSeats;
        }
        for (Coach coach : train.getCoaches()) {
            int occupiedSeatCount = coach.getTotalSeatCount() - coach.getAvailableSeats().size();
            double coachOccupancy = (double) (occupiedSeatCount) / coach.getTotalSeatCount();
            Reservation bookedSeats = doMakeReservation(request, bookingId, coach, coachOccupancy);
            if (bookedSeats != null) return bookedSeats;
        }

        return new Reservation(request.getTrainId(), List.of(), "");
    }

    private Reservation doMakeReservation(ReservationRequest request, String bookingId, Coach coach, double coachOccupancy) {
        if (coachOccupancy <= MAX_TRAIN_OCCUPANCY_RATE) {
            List<String> bookedSeats = coach.getAvailableSeats().stream()
                    .limit(request.getSeatCount())
                    .map(n -> "" + n + coach.getCoach())
                    .collect(Collectors.toList());
            trainDataGateway.reserve(request.getTrainId(), bookedSeats, bookingId);
            return new Reservation(request.getTrainId(), bookedSeats, bookingId);
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