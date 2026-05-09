package edu.sjsu.cmpe172.starterdemo.model;

public class CalendarEventResponse {

    private String eventId;
    private String status;
    private String message;

    public CalendarEventResponse() {}

    public CalendarEventResponse(String eventId, String status, String message) {
        this.eventId = eventId;
        this.status = status;
        this.message = message;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
