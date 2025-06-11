package com.example.springboot;

public class MeterReading {
    private String id;
    private long timestamp;
    private double value;

    public MeterReading() {}

    public MeterReading(String id, long timestamp, double value) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}
