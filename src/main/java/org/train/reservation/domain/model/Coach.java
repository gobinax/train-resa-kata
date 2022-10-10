package org.train.reservation.domain.model;

import java.util.List;

public record Coach(String coach, int totalSeatCount, List<Integer> availableSeats) {

    private static final double MAX_COACH_OCCUPANCY_RATE = .7;

    public List<Seat> reserve(int seatCount) {
        return availableSeats.stream().limit(seatCount).map(seatNumber -> new Seat(coach, seatNumber)).toList();
    }

    public boolean occupancyUnderThreshold(int seatCount) {
        return occupancy(seatCount) <= MAX_COACH_OCCUPANCY_RATE;
    }

    public double occupancy(int seatCount) {
        return (double) (totalSeatCount - availableSeats.size() + seatCount) / totalSeatCount;
    }
}
