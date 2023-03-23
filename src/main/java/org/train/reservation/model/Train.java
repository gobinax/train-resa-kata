package org.train.reservation.model;

import java.util.Arrays;
import java.util.List;

public class Train {

    private final List<Coach> coaches;

    public Train(Coach... coach) {
        this(Arrays.asList(coach));
    }

    public  Train(List<Coach> coaches) {
        this.coaches = coaches;
    }

    public List<Coach> getCoaches() {
        return coaches;
    }
}
