package org.train.reservation.adapter.secondary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.train.reservation.domain.model.Seat;
import org.train.reservation.domain.model.Train;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RestTrainDataGatewayIT {

    @Autowired
    RestTrainDataGateway trainDataGateway;

    @Test
    void should_retrieve_train_data() {
        Train train = trainDataGateway.getTrainData("local_1000");
        assertThat(train).isNotNull();
        assertThat(train.getCoaches()).isNotEmpty();
    }

    @Test
    void should_reserve_seats() {
        Train train = trainDataGateway.reserve(
                "local_1000",
                List.of(new Seat("A", 1), new Seat("A", 2)),
                "75bcd16");
        assertThat(train).isNotNull();
        assertThat(train.getCoaches()).isNotEmpty();
    }
}