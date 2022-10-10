package org.train.reservation.adapter.secondary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.train.reservation.domain.model.Seat;
import org.train.reservation.domain.model.Train;
import org.train.reservation.domain.port.TrainDataGateway;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RestTrainDataGateway implements TrainDataGateway {

    @Autowired
    private RestTemplate trainDataClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Train getTrainData(String trainId) {
        try {
            return doGetTrainData(trainId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Train doGetTrainData(String trainId) throws JsonProcessingException {
        String jsonStringResponse = trainDataClient.getForObject("/data_for_train/" + trainId, String.class);
        TrainDataDto trainDataDto = objectMapper.readValue(jsonStringResponse, TrainDataDto.class);
        return trainDataDto.mapToTrain();
    }

    @Override
    public Train reserve(String trainId, List<Seat> seats, String bookingId) {
        try {
            return doReserve(trainId, seats, bookingId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Train doReserve(String trainId, List<Seat> seats, String bookingId) throws JsonProcessingException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("train_id", trainId);
//        map.add("seats", seats.toString());
//        map.add("booking_reference", bookingId);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String jsonStringResponse = trainDataClient.postForObject("/reserve", Map.of(
                "train_id", trainId,
                "seats", seats.stream().map(Seat::toString).collect(Collectors.joining(",", "[", "]")),
                "booking_reference", bookingId
        ), String.class);
        TrainDataDto trainDataDto = objectMapper.readValue(jsonStringResponse, TrainDataDto.class);
        return trainDataDto.mapToTrain();
    }
}
