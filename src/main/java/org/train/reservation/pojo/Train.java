package org.train.reservation.pojo;

import java.util.Map;

public class Train {
    private Map<String, Seat> seats;

    public Map<String, Seat> getSeats() {
        return seats;
    }

    public void setSeats(Map<String, Seat> seats) {
        this.seats = seats;
    }
}

