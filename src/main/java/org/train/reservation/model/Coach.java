package org.train.reservation.model;

import java.util.List;

public class Coach {

    private final String coach;
    private final int totalSeatCount;
    private final List<Integer> availableSeats;

    public Coach(String coach, int totalSeatCount, List<Integer> availableSeats) {
        this.coach = coach;
        this.totalSeatCount = totalSeatCount;
        this.availableSeats = availableSeats;
    }

    public String getCoach() {
        return coach;
    }

    public int getTotalSeatCount() {
        return totalSeatCount;
    }

    public List<Integer> getAvailableSeats() {
        return availableSeats;
    }
}