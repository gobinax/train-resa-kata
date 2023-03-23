package org.train.reservation.domain.model;

import java.util.Objects;

public final class Seat {
    private final String coach;
    private final int seatNumber;

    public Seat(String coach, int seatNumber) {
        this.coach = coach;
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return coach + seatNumber;
    }

    public String coach() {
        return coach;
    }

    public int seatNumber() {
        return seatNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Seat) obj;
        return Objects.equals(this.coach, that.coach) &&
                this.seatNumber == that.seatNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coach, seatNumber);
    }

}