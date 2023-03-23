package org.train.reservation.pojo;


import java.util.List;

public class Reservation {
	public final String trainId;
    public final String bookingId;
    public final List<String> seats;

    public Reservation(String trainId, List<String> seats, String bookingId) {
		this.trainId = trainId;
        this.bookingId = bookingId;
        this.seats = seats;
    }

}
