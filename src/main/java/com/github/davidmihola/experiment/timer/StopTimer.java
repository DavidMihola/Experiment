package com.github.davidmihola.experiment.timer;

/**
 * Created by david on 15.03.14.
 */
import org.joda.time.DateTime;
import org.joda.time.Interval;

public final class StopTimer {

    private DateTime start;

    public StopTimer() {
        this.start = DateTime.now();
    }

    public Interval getTime() {
        return new Interval(start, DateTime.now());
    }

}