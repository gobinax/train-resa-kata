package org.train.reservation.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TrainTest {

    @Test
    void train_should_reserve_no_seats_if_makes_overall_over_threshold() {
        // GIVEN
        Train train = new Train(List.of(
                // 8/10 occupancy
                new Coach("A", 10, List.of(1, 2)),
                // 6/10 occupancy
                new Coach("B", 10, List.of(1, 2, 3, 4))
        )); // => overall 70% occupancy

        // WHEN
        List<Seat> reserve = train.reserve(1);

        // THEN
        assertThat(reserve).isEmpty();
    }

    @Test
    void train_should_reserve_seats_if_train_occupancy_under_70_and_prefer_coach_under_70_percent_occupancy() {
        // GIVEN
        Train train = new Train(List.of(
                // 8/10 occupancy => over 70%
                new Coach("A", 10, List.of(1, 2)),
                // 6/10 occupancy => +2 makes it over 70%
                new Coach("B", 10, List.of(1, 2, 3, 4)),
                // 5/10 occupancy +2 makes it 70%
                new Coach("C", 10, List.of(1, 2, 3, 4, 5))
        ));

        // WHEN
        List<Seat> reserve = train.reserve(2);

        // THEN
        assertThat(reserve).containsExactly(
                new Seat("C", 1),
                new Seat("C", 2));
    }

    @Test
    public void train_should_overbook_coach_over_70_if_keep_train_occupancy_under_70() {
        // GIVEN
        Train train = new Train(List.of(
                // 6/10 seats available
                new Coach("A", 10, List.of(1, 2, 3, 4)),
                // 6/10 seats available
                new Coach("B", 10, List.of(1, 2, 3, 4))
        ));

        // WHEN
        List<Seat> reserve = train.reserve(2);

        // THEN
        assertThat(reserve).containsExactly(
                new Seat("A", 1),
                new Seat("A", 2));
    }
}