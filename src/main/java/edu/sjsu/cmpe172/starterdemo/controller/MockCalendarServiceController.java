package edu.sjsu.cmpe172.starterdemo.controller;

import edu.sjsu.cmpe172.starterdemo.model.CalendarEventRequest;
import edu.sjsu.cmpe172.starterdemo.model.CalendarEventResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Mock Provider Calendar Service.
 *
 * Responsibility: Manages the provider (stylist) side of the calendar.
 * In production this would be a separate deployed service (e.g., a Google Calendar integration).
 *
 * Coarse-grained API surface:
 *   GET  /mock-calendar/availability/{stylistName}  → returns open time blocks for a stylist
 *   POST /mock-calendar/register                    → registers a confirmed appointment event
 *
 * Both endpoints accept/return complete, self-contained payloads so the caller
 * never needs multiple round-trips to accomplish a single business operation.
 */
@RestController
@RequestMapping("/mock-calendar")
public class MockCalendarServiceController {

    /**
     * GET /mock-calendar/availability/{stylistName}
     *
     * Returns a simulated list of open time blocks on the provider's calendar.
     * In a real system this would query the stylist's calendar system.
     */
    @GetMapping("/availability/{stylistName}")
    public ResponseEntity<Map<String, Object>> getAvailability(@PathVariable String stylistName) {
        List<String> openSlots = List.of("09:00 AM", "11:00 AM", "02:00 PM", "04:00 PM");
        Map<String, Object> response = Map.of(
            "stylist", stylistName,
            "availableSlots", openSlots,
            "note", "Mock availability — replace with real calendar integration"
        );
        return ResponseEntity.ok(response);
    }

    /**
     * POST /mock-calendar/register
     *
     * Accepts a complete appointment payload and registers it as a calendar event
     * on the provider's calendar. Returns a confirmation with a generated event ID.
     */
    @PostMapping("/register")
    public CalendarEventResponse registerAppointment(@RequestBody CalendarEventRequest request) {
        String eventId = "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        String message = String.format(
            "Appointment for %s with %s on %s at %s (%s) added to provider calendar.",
            request.getCustomerName(),
            request.getStylistName(),
            request.getSlotDate(),
            request.getStartTime(),
            request.getService()
        );

        return new CalendarEventResponse(eventId, "CONFIRMED", message);
    }
}
