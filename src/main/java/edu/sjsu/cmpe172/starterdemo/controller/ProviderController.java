package edu.sjsu.cmpe172.starterdemo.controller;

import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import edu.sjsu.cmpe172.starterdemo.model.TimeSlot;
import edu.sjsu.cmpe172.starterdemo.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/provider")
public class ProviderController {

    private static final String PROVIDER_PIN = "1234";
    private static final String SESSION_KEY  = "providerAuth";

    private final AppointmentService appointmentService;

    public ProviderController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "provider-login";
    }

    @PostMapping("/login")
    public String submitPin(@RequestParam String pin, HttpSession session, Model model) {
        if (PROVIDER_PIN.equals(pin)) {
            session.setAttribute(SESSION_KEY, true);
            return "redirect:/provider";
        }
        model.addAttribute("error", "Incorrect PIN. Please try again.");
        return "provider-login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
        return "redirect:/";
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (!Boolean.TRUE.equals(session.getAttribute(SESSION_KEY))) {
            return "redirect:/provider/login";
        }
        List<TimeSlot> slots = appointmentService.getAllSlots();
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("slots", slots);
        model.addAttribute("appointments", appointments);
        model.addAttribute("newSlot", new TimeSlot());
        return "provider";
    }

    @PostMapping("/slots")
    public String addSlot(@ModelAttribute TimeSlot newSlot, HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute(SESSION_KEY))) {
            return "redirect:/provider/login";
        }
        appointmentService.addSlot(newSlot);
        return "redirect:/provider";
    }

    @PostMapping("/slots/{slotId}/delete")
    public String deleteSlot(@PathVariable Long slotId, HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute(SESSION_KEY))) {
            return "redirect:/provider/login";
        }
        appointmentService.deleteSlot(slotId);
        return "redirect:/provider";
    }

    @PostMapping("/cancel/{appointmentId}")
    public String cancelAppointment(@PathVariable Long appointmentId, HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute(SESSION_KEY))) {
            return "redirect:/provider/login";
        }
        appointmentService.cancelAppointment(appointmentId);
        return "redirect:/provider";
    }
}
