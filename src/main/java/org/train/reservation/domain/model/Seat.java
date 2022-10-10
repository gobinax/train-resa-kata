package org.train.reservation.domain.model;

public record Seat(String coach, int seatNumber) {

    @Override
    public String toString() {
        return coach + seatNumber;
    }
}