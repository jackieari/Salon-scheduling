package edu.sjsu.cmpe172.starterdemo.model;

public class Appointment {

    private Long appointmentId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Long slotId;
    private String stylistName;
    private String slotDate;
    private String startTime;
    private String service;

    public Appointment() {}

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }

    public String getStylistName() { return stylistName; }
    public void setStylistName(String stylistName) { this.stylistName = stylistName; }

    public String getSlotDate() { return slotDate; }
    public void setSlotDate(String slotDate) { this.slotDate = slotDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
}
