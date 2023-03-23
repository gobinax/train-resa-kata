package org.train.reservation.domain.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Train {

    private static final double MAX_TRAIN_OCCUPANCY_RATE = .7;

    private final List<Coach> coaches;

    public Train(List<Coach> coaches) {
        this.coaches = coaches;
    }

    public List<Seat> reserve(int seatCount) {
        double occupancy = occupancy(seatCount);
        if (occupancy > MAX_TRAIN_OCCUPANCY_RATE) {
            return List.of();
        }

        return coachUnderOccupancyThreshold(seatCount)
                .or(() -> coachUnderOccupancyThreshold(0))
                .map(coach -> coach.reserve(seatCount))
                .orElse(List.of());

    }

    /**
     * find a coach which will stay under occupancy threshold if seatCount seats are booked
     */
    private Optional<Coach> coachUnderOccupancyThreshold(int seatCount) {
        return coaches.stream()
                .filter(coach -> coach.occupancyUnderThreshold(seatCount))
                .findFirst();
    }

    private double occupancy(int seatCount) {
        int totalCount = 0;
        int availableCount = 0;
        for (Coach coach : coaches()) {
            totalCount += coach.totalSeatCount();
            availableCount += coach.availableSeats().size();
        }
        return (double) (totalCount - availableCount + seatCount) / totalCount;
    }

    public List<Coach> coaches() {
        return coaches;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Train) obj;
        return Objects.equals(this.coaches, that.coaches);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coaches);
    }

    @Override
    public String toString() {
        return "Train[" +
                "coaches=" + coaches + ']';
    }

}
