package org.train.reservation.domain.port;

import org.train.reservation.domain.model.Seat;
import org.train.reservation.domain.model.Train;

import java.util.List;

public interface TrainDataGateway {
    Train getTrainData(String trainId);
    Train reserve(String trainId, List<Seat> seats, String bookingId);
}