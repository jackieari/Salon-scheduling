package edu.sjsu.cmpe172.starterdemo.controller;

import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import edu.sjsu.cmpe172.starterdemo.model.TimeSlot;
import edu.sjsu.cmpe172.starterdemo.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SalonController {

    private static final Logger log = LoggerFactory.getLogger(SalonController.class);

    private final AppointmentService appointmentService;

    public SalonController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/slots")
    public String viewSlots(Model model) {
        List<TimeSlot> slots = appointmentService.getAvailableSlots();
        model.addAttribute("slots", slots);
        return "slots";
    }

    @GetMapping("/book/{slotId}")
    public String showBookingForm(@PathVariable Long slotId, Model model) {
        TimeSlot slot = appointmentService.getSlotById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found: " + slotId));

        Appointment appointment = new Appointment();
        appointment.setSlotId(slotId);

        model.addAttribute("slot", slot);
        model.addAttribute("appointment", appointment);
        return "book";
    }

    @PostMapping("/book")
    public String submitBooking(@ModelAttribute Appointment appointment, Model model) {
        log.info("event=booking_attempt slot_id={} customer={}", appointment.getSlotId(), appointment.getCustomerEmail());
        try {
            Appointment saved = appointmentService.bookAppointment(appointment);
            return "redirect:/confirmation/" + saved.getAppointmentId();
        } catch (IllegalStateException e) {
            log.warn("event=booking_rejected slot_id={} customer={} reason={}",
                    appointment.getSlotId(), appointment.getCustomerEmail(), e.getMessage());
            model.addAttribute("error", e.getMessage());
            TimeSlot slot = appointmentService.getSlotById(appointment.getSlotId()).orElse(null);
            model.addAttribute("slot", slot);
            model.addAttribute("appointment", appointment);
            return "book";
        }
    }

    @GetMapping("/confirmation/{id}")
    public String showConfirmation(@PathVariable Long id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));
        model.addAttribute("appointment", appointment);
        return "confirmation";
    }

    @PostMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return "redirect:/slots";
    }
}
