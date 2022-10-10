package org.train.reservation.domain.service;

import org.train.reservation.domain.model.Seat;

import java.util.List;

public record Reservation(String trainId, List<Seat> seats, String bookingId) {

}
