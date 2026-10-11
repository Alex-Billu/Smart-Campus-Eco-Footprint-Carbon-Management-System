package com.campus.eco.model;

import java.time.LocalDate;

public class GreenEvent {
    private int eventId;
    private String eventName;
    private String eventType;
    private LocalDate eventDate;
    private String location;
    private String description;
    private int greenPoints;

    public GreenEvent() {}

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getGreenPoints() { return greenPoints; }
    public void setGreenPoints(int greenPoints) { this.greenPoints = greenPoints; }
}
