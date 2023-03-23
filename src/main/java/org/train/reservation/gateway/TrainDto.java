package org.train.reservation.gateway;

import org.apache.logging.log4j.util.Strings;
import org.train.reservation.model.Coach;
import org.train.reservation.model.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrainDto {
    public Map<String, SeatDto> seats;

    public Train mapToTrain() {
        List<Coach> coaches = new ArrayList<>();
        seats.values().stream()
                .collect(Collectors.groupingBy(SeatDto::getCoach, Collectors.toList()))
                .forEach((coach, seats) -> {
                    List<Integer> availableSeats = seats.stream()
                            .filter(s -> Strings.isBlank(s.getBooking_reference()))
                            .map(SeatDto::getSeat_number)
                            .collect(Collectors.toList());
                    coaches.add(new Coach(coach, seats.size(), availableSeats));
                });
        return new Train(coaches);
    }
}

