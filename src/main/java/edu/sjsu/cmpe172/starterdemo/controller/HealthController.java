package edu.sjsu.cmpe172.starterdemo.controller;

import edu.sjsu.cmpe172.starterdemo.monitoring.BookingMetrics;
import edu.sjsu.cmpe172.starterdemo.repository.SlotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GET /health — production health check endpoint.
 *
 * Returns HTTP 200 with status:"UP" when all subsystems are healthy.
 * Returns HTTP 503 with status:"DOWN" if any critical check fails.
 * Designed to be polled by a load balancer or uptime monitor every 30 seconds.
 */
@RestController
public class HealthController {

    private final SlotRepository slotRepository;
    private final BookingMetrics bookingMetrics;

    public HealthController(SlotRepository slotRepository, BookingMetrics bookingMetrics) {
        this.slotRepository = slotRepository;
        this.bookingMetrics = bookingMetrics;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> result = new LinkedHashMap<>();

        boolean repositoryOk = checkRepository();
        boolean overall = repositoryOk;

        result.put("status", overall ? "UP" : "DOWN");
        result.put("uptimeSeconds", ManagementFactory.getRuntimeMXBean().getUptime() / 1000);

        Map<String, Object> checks = new LinkedHashMap<>();
        checks.put("slotRepository", repositoryOk ? "UP" : "DOWN");
        result.put("checks", checks);

        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("totalBookings",          bookingMetrics.getTotalBookings());
        metrics.put("failedBookings",         bookingMetrics.getFailedBookings());
        metrics.put("averageBookingLatencyMs", String.format("%.1f", bookingMetrics.getAverageLatencyMs()));
        result.put("metrics", metrics);

        return overall
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(503).body(result);
    }

    private boolean checkRepository() {
        try {
            slotRepository.findAvailable();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
