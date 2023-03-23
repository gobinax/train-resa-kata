package org.train.reservation.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.train.reservation.model.Train;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TrainDataGateway {

    @Autowired
    private RestTemplate trainDataClient;

    @Autowired
    private ObjectMapper objectMapper;

    public Train getTrainData(String trainId) {
        try {
            return doGetTrainData(trainId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Train doGetTrainData(String trainId) throws JsonProcessingException {
        String jsonStringResponse = trainDataClient.getForObject("/data_for_train/" + trainId, String.class);
        TrainDto trainDataDto = objectMapper.readValue(jsonStringResponse, TrainDto.class);
        return trainDataDto.mapToTrain();
    }

    public Train reserve(String trainId, List<String> seats, String bookingId) {
        try {
            return doReserve(trainId, seats, bookingId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Train doReserve(String trainId, List<String> seats, String bookingId) throws JsonProcessingException {

        String jsonStringResponse = trainDataClient.postForObject("/reserve", Map.of(
                "train_id", trainId,
                "seats", seats.stream().collect(Collectors.joining(",", "[", "]")),
                "booking_reference", bookingId
        ), String.class);
        TrainDto trainDataDto = objectMapper.readValue(jsonStringResponse, TrainDto.class);
        return trainDataDto.mapToTrain();
    }
}
