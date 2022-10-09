package org.train.reservation.domain.model;

public class Seat {
    public final String coach;
    public final int seatNumber;

    public Seat(String coach, int seatNumber) {
        this.coach = coach;
        this.seatNumber = seatNumber;
    }

    public boolean equals(Object o) {
        Seat other = (Seat)o;
        return coach==other.coach && seatNumber==other.seatNumber;
    }

    @Override
    public String toString() {
        return coach + seatNumber;
    }
}