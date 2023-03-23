package org.train.reservation.domain.service;

import org.train.reservation.domain.model.Seat;

import java.util.List;
import java.util.Objects;

public final class Reservation {
    private final String trainId;
    private final List<Seat> seats;
    private final String bookingId;

    public Reservation(String trainId, List<Seat> seats, String bookingId) {
        this.trainId = trainId;
        this.seats = seats;
        this.bookingId = bookingId;
    }

    public String getTrainId() {
        return trainId;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public String getBookingId() {
        return bookingId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Reservation) obj;
        return Objects.equals(this.trainId, that.trainId) &&
                Objects.equals(this.seats, that.seats) &&
                Objects.equals(this.bookingId, that.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainId, seats, bookingId);
    }

    @Override
    public String toString() {
        return "Reservation[" +
                "trainId=" + trainId + ", " +
                "seats=" + seats + ", " +
                "bookingId=" + bookingId + ']';
    }


}
