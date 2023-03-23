package org.train.reservation.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.train.reservation.model.Coach;
import org.train.reservation.model.Train;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TrainDtoTest {
    @Test
    void should_map_to_train() throws IOException {
        // given
        String jsonStr = IOUtils.resourceToString("/train_data.json", StandardCharsets.UTF_8);
        TrainDto trainDataDto = new ObjectMapper().readValue(jsonStr, TrainDto.class);

        // when
        Train train = trainDataDto.mapToTrain();

        // then
        assertThat(train.getCoaches()).extracting(Coach::getCoach).containsExactly("A", "B");
        assertThat(train.getCoaches()).extracting(Coach::getTotalSeatCount).containsExactly(8, 8);
        assertThat(train.getCoaches()).extracting(Coach::getAvailableSeats).containsExactly(
                List.of(6, 7, 8),
                List.of(5, 6, 7, 8));
    }
}