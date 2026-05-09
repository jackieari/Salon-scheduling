package edu.sjsu.cmpe172.starterdemo.service;

import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import edu.sjsu.cmpe172.starterdemo.model.TimeSlot;
import edu.sjsu.cmpe172.starterdemo.monitoring.BookingMetrics;
import edu.sjsu.cmpe172.starterdemo.repository.AppointmentRepository;
import edu.sjsu.cmpe172.starterdemo.repository.SlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepo;
    private final SlotRepository slotRepo;
    private final BookingMetrics bookingMetrics;

    public AppointmentService(AppointmentRepository appointmentRepo,
                              SlotRepository slotRepo,
                              BookingMetrics bookingMetrics) {
        this.appointmentRepo = appointmentRepo;
        this.slotRepo = slotRepo;
        this.bookingMetrics = bookingMetrics;
    }

    public List<TimeSlot> getAvailableSlots() {
        List<TimeSlot> slots = slotRepo.findAvailable();
        log.info("event=slots_listed available_count={}", slots.size());
        return slots;
    }

    public List<TimeSlot> getAllSlots() {
        return slotRepo.findAll();
    }

    public Optional<TimeSlot> getSlotById(Long slotId) {
        return slotRepo.findById(slotId);
    }

    @Transactional
    public synchronized Appointment bookAppointment(Appointment appointment) {
        long startMs = System.currentTimeMillis();

        TimeSlot slot = slotRepo.findById(appointment.getSlotId()).orElse(null);

        if (slot == null) {
            bookingMetrics.recordFailure();
            log.error("event=booking_failed reason=slot_not_found slot_id={} customer={}",
                    appointment.getSlotId(), appointment.getCustomerEmail());
            throw new IllegalArgumentException("Slot not found: " + appointment.getSlotId());
        }

        if (slot.isBlocked()) {
            bookingMetrics.recordFailure();
            log.warn("event=booking_failed reason=slot_unavailable slot_id={} stylist={} customer={}",
                    slot.getSlotId(), slot.getStylistName(), appointment.getCustomerEmail());
            throw new IllegalStateException("Slot is no longer available.");
        }

        appointment.setStylistName(slot.getStylistName());
        appointment.setSlotDate(slot.getSlotDate());
        appointment.setStartTime(slot.getStartTime());
        appointment.setService(slot.getService());

        slot.setBlocked(true);
        slotRepo.save(slot);

        Appointment saved = appointmentRepo.save(appointment);
        long latencyMs = System.currentTimeMillis() - startMs;
        bookingMetrics.recordSuccess(latencyMs);

        log.info("event=booking_confirmed appointment_id={} slot_id={} stylist={} service={} customer={} latency_ms={}",
                saved.getAppointmentId(), slot.getSlotId(), slot.getStylistName(),
                slot.getService(), saved.getCustomerEmail(), latencyMs);

        return saved;
    }

    @Transactional
    public synchronized void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));

        slotRepo.findById(appointment.getSlotId()).ifPresent(slot -> {
            slot.setBlocked(false);
            slotRepo.save(slot);
        });

        appointmentRepo.deleteById(appointmentId);
        log.info("event=booking_cancelled appointment_id={} slot_id={}", appointmentId, appointment.getSlotId());
    }

    public Optional<Appointment> getAppointmentById(Long appointmentId) {
        return appointmentRepo.findById(appointmentId);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public TimeSlot addSlot(TimeSlot slot) {
        TimeSlot saved = slotRepo.insert(slot);
        log.info("event=slot_added slot_id={} stylist={} date={} time={}",
                saved.getSlotId(), saved.getStylistName(), saved.getSlotDate(), saved.getStartTime());
        return saved;
    }

    public void deleteSlot(Long slotId) {
        slotRepo.deleteById(slotId);
        log.info("event=slot_deleted slot_id={}", slotId);
    }
}
