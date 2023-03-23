package org.train.reservation.pojo;

public class ReservationRequest {
	private final String trainId;
    private final int seatCount;

    public ReservationRequest(String trainId, int seatCount) {
		this.trainId = trainId;
        this.seatCount = seatCount;
    }

    public String getTrainId() {
        return trainId;
    }

    public int getSeatCount() {
        return seatCount;
    }
}