package org.train.reservation.domain.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Coach {

    private static final double MAX_COACH_OCCUPANCY_RATE = .7;

    private final String coach;
    private final int totalSeatCount;
    private final List<Integer> availableSeats;

    public Coach(String coach, int totalSeatCount, List<Integer> availableSeats) {
        this.coach = coach;
        this.totalSeatCount = totalSeatCount;
        this.availableSeats = availableSeats;
    }

    public List<Seat> reserve(int seatCount) {
        return availableSeats.stream()
                .limit(seatCount)
                .map(seatNumber -> new Seat(coach, seatNumber))
                .collect(Collectors.toList());
    }

    public boolean occupancyUnderThreshold(int seatCount) {
        return occupancy(seatCount) <= MAX_COACH_OCCUPANCY_RATE;
    }

    public double occupancy(int seatCount) {
        return (double) (totalSeatCount - availableSeats.size() + seatCount) / totalSeatCount;
    }

    public String coach() {
        return coach;
    }

    public int totalSeatCount() {
        return totalSeatCount;
    }

    public List<Integer> availableSeats() {
        return availableSeats;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Coach) obj;
        return Objects.equals(this.coach, that.coach) &&
                this.totalSeatCount == that.totalSeatCount &&
                Objects.equals(this.availableSeats, that.availableSeats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coach, totalSeatCount, availableSeats);
    }

    @Override
    public String toString() {
        return "Coach[" +
                "coach=" + coach + ", " +
                "totalSeatCount=" + totalSeatCount + ", " +
                "availableSeats=" + availableSeats + ']';
    }

}
