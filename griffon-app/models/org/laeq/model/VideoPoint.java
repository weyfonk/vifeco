package org.laeq.model;

import javafx.util.Duration;

public class VideoPoint {
    private final double x;
    private final double y;
    private final int size;
    private final int duration;
    private final Duration start;
    private final Duration end;
    private final Category category;

    public VideoPoint(double x, double y, int size, int duration, Duration start, Category category) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.duration = duration;
        this.start = start;
        this.end = Duration.seconds(start.toSeconds() + duration);
        this.category = category;
        System.out.println(this.start);
        System.out.println(this.end);
    }

    public Boolean isValid(Duration now){
        return this.end.greaterThanOrEqualTo(now);
    }
}
