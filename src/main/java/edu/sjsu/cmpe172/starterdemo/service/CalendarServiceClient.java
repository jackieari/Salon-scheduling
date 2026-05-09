package edu.sjsu.cmpe172.starterdemo.service;

import edu.sjsu.cmpe172.starterdemo.model.CalendarEventRequest;
import edu.sjsu.cmpe172.starterdemo.model.CalendarEventResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CalendarServiceClient {

    private final RestTemplate restTemplate;

    // In a real system this would point to a deployed external service URL.
    // Here it calls our mock controller running in the same app.
    private static final String CALENDAR_SERVICE_URL = "http://localhost:8081/mock-calendar/register";

    public CalendarServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CalendarEventResponse registerAppointment(CalendarEventRequest request) {
        return restTemplate.postForObject(CALENDAR_SERVICE_URL, request, CalendarEventResponse.class);
    }
}
