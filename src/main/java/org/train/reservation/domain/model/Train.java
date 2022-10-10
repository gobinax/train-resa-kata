package org.train.reservation.domain.model;

import java.util.List;
import java.util.Optional;

public record Train(List<Coach> coaches) {

    private static final double MAX_TRAIN_OCCUPANCY_RATE = .7;

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
}
