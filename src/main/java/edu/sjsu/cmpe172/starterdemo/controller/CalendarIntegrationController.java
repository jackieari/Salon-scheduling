package edu.sjsu.cmpe172.starterdemo.controller;

import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import edu.sjsu.cmpe172.starterdemo.model.CalendarEventRequest;
import edu.sjsu.cmpe172.starterdemo.model.CalendarEventResponse;
import edu.sjsu.cmpe172.starterdemo.service.AppointmentService;
import edu.sjsu.cmpe172.starterdemo.service.CalendarServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Coarse-grained REST endpoint in the main Appointment Scheduler.
 *
 * Design justification:
 *  - Single endpoint syncs an entire appointment to the provider calendar in one call.
 *  - The caller (browser or internal trigger) only needs the appointmentId —
 *    all detail retrieval and request construction happen inside this boundary.
 *  - Coarse granularity hides internal data model from the external service and
 *    reduces network round-trips (no separate "get details" + "post event" calls).
 */
@RestController
@RequestMapping("/api/calendar")
public class CalendarIntegrationController {

    private final AppointmentService appointmentService;
    private final CalendarServiceClient calendarServiceClient;

    public CalendarIntegrationController(AppointmentService appointmentService,
                                         CalendarServiceClient calendarServiceClient) {
        this.appointmentService = appointmentService;
        this.calendarServiceClient = calendarServiceClient;
    }

    /**
     * POST /api/calendar/sync/{appointmentId}
     *
     * Looks up the booked appointment, packages all details into a single
     * CalendarEventRequest, and delegates to the external calendar service.
     * Returns the calendar service's confirmation (eventId + status).
     */
    @PostMapping("/sync/{appointmentId}")
    public ResponseEntity<CalendarEventResponse> syncToCalendar(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId)
                .orElse(null);

        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }

        CalendarEventRequest request = new CalendarEventRequest(
            appointment.getCustomerName(),
            appointment.getStylistName(),
            appointment.getSlotDate(),
            appointment.getStartTime(),
            appointment.getService()
        );

        CalendarEventResponse response = calendarServiceClient.registerAppointment(request);
        return ResponseEntity.ok(response);
    }
}
