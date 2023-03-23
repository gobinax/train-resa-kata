package org.train.reservation.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.train.reservation.domain.model.Coach;
import org.train.reservation.domain.model.Train;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TrainDataDtoTest {

    @Test
    void should_map_to_train() throws IOException {
        // given
        String jsonStr = IOUtils.resourceToString("/train_data.json", StandardCharsets.UTF_8);
        TrainDataDto trainDataDto = new ObjectMapper().readValue(jsonStr, TrainDataDto.class);

        // when
        Train train = trainDataDto.mapToTrain();

        // then
        assertThat(train.coaches()).extracting(Coach::coach).containsExactly("A", "B");
        assertThat(train.coaches()).extracting(Coach::totalSeatCount).containsExactly(8, 8);
        assertThat(train.coaches()).extracting(Coach::availableSeats).containsExactly(
                List.of(6, 7, 8),
                List.of(5, 6, 7, 8));
    }
}