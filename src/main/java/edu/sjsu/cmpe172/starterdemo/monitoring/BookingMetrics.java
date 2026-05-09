package edu.sjsu.cmpe172.starterdemo.monitoring;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory counters for booking operations.
 * In a real deployment these would be exported to Prometheus/Micrometer.
 */
@Component
public class BookingMetrics {

    private final AtomicLong totalBookings = new AtomicLong();
    private final AtomicLong failedBookings = new AtomicLong();
    private final AtomicLong totalBookingLatencyMs = new AtomicLong();

    public void recordSuccess(long latencyMs) {
        totalBookings.incrementAndGet();
        totalBookingLatencyMs.addAndGet(latencyMs);
    }

    public void recordFailure() {
        failedBookings.incrementAndGet();
    }

    public long getTotalBookings()  { return totalBookings.get(); }
    public long getFailedBookings() { return failedBookings.get(); }

    public double getAverageLatencyMs() {
        long count = totalBookings.get();
        return count == 0 ? 0.0 : (double) totalBookingLatencyMs.get() / count;
    }
}
