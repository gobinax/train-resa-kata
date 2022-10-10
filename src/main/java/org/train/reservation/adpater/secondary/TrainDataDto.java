package org.train.reservation.adpater.secondary;

import org.apache.logging.log4j.util.Strings;
import org.train.reservation.domain.model.Coach;
import org.train.reservation.domain.model.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrainDataDto {
    public Map<String, SeatDto> seats;

    public Train mapToTrain() {
        List<Coach> coaches = new ArrayList<>();
        seats.values().stream()
                .collect(Collectors.groupingBy(seat -> seat.coach, Collectors.toList()))
                .forEach((coach, seats) -> {
                    List<Integer> availableSeats = seats.stream()
                            .filter(s -> Strings.isBlank(s.booking_reference))
                            .map(s -> s.seat_number)
                            .toList();
                    coaches.add(new Coach(coach, seats.size(), availableSeats));
                });
        return new Train(coaches);
    }
}

class SeatDto {
    public String coach;
    public Integer seat_number;
    public String booking_reference;
}
