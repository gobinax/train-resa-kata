package org.train.reservation.service;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.train.reservation.gateway.BookingReferenceGateway;
import org.train.reservation.gateway.TrainDataGateway;
import org.train.reservation.pojo.Reservation;
import org.train.reservation.pojo.ReservationRequest;
import org.train.reservation.pojo.Seat;
import org.train.reservation.pojo.Train;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class ReservationService {

    private static final double MAX_TRAIN_OCCUPANCY_RATE = .7;

    @Autowired
    private BookingReferenceGateway bookingReferenceGateway;

    @Autowired
    private TrainDataGateway trainDataGateway;

    public Reservation makeReservation(ReservationRequest request) {
        String bookingId = bookingReferenceGateway.retrieveBookingReference();
        Train train = trainDataGateway.getTrainData(request.getTrainId());

        double trainOccupancy = computeFutureTrainOccupancy(train, request.getSeatCount());
        if (trainOccupancy > MAX_TRAIN_OCCUPANCY_RATE) {
            return new Reservation(request.getTrainId(), List.of(), "");
        }

        Map<String, List<Seat>> seatsByCoach = train.getSeats().values().stream()
                .collect(Collectors.groupingBy(seat -> seat.getCoach(), toList()));


        for (Map.Entry<String, List<Seat>> stringListEntry : seatsByCoach.entrySet()) {
            List<Seat> coachSeats = stringListEntry.getValue();
            long occupiedSeatCount = coachSeats.size() - coachSeats.stream().filter(seat -> Strings.isBlank(seat.getBooking_reference())).count();
            double coachOccupancy = (double) (request.getSeatCount() + occupiedSeatCount) / coachSeats.size();

            Reservation bookedSeats = doMakeReservation(request, bookingId, coachSeats, coachOccupancy);
            if (bookedSeats != null) return bookedSeats;
        }
        for (Map.Entry<String, List<Seat>> stringListEntry : seatsByCoach.entrySet()) {
            List<Seat> coachSeats = stringListEntry.getValue();
            long occupiedSeatCount = coachSeats.size() - coachSeats.stream().filter(seat -> Strings.isBlank(seat.getBooking_reference())).count();
            double coachOccupancy = (double) (occupiedSeatCount) / coachSeats.size();

            Reservation bookedSeats = doMakeReservation(request, bookingId, coachSeats, coachOccupancy);
            if (bookedSeats != null) return bookedSeats;
        }

        return new Reservation(request.getTrainId(), List.of(), "");
    }

    private Reservation doMakeReservation(ReservationRequest request, String bookingId, List<Seat> coachSeats, double coachOccupancy) {
        if (coachOccupancy <= MAX_TRAIN_OCCUPANCY_RATE) {

            List<Seat> bookedSeats = coachSeats.stream()
                    .filter(seat -> Strings.isBlank(seat.getBooking_reference()))
                    .limit(request.getSeatCount())
                    .collect(toList());
            trainDataGateway.reserve(request.getTrainId(), bookedSeats, bookingId);
            List<String> seatsStr = bookedSeats.stream()
                    .map(s -> "" + s.getSeat_number() + s.getCoach())
                    .collect(toList());
            return new Reservation(request.getTrainId(), seatsStr, bookingId);
        }
        return null;
    }

    private double computeFutureTrainOccupancy(Train train, int seatCount) {
        int totalCount = 0;
        int availableCount = 0;

        for (Seat seat : train.getSeats().values()) {
            if (Strings.isBlank(seat.getBooking_reference())) {
                availableCount++;
            }
            totalCount++;
        }
        return (double) (totalCount - availableCount + seatCount) / totalCount;
    }
}