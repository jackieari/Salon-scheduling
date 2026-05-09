package edu.sjsu.cmpe172.starterdemo.model;

public class CalendarEventRequest {

    private String customerName;
    private String stylistName;
    private String slotDate;
    private String startTime;
    private String service;

    public CalendarEventRequest() {}

    public CalendarEventRequest(String customerName, String stylistName,
                                String slotDate, String startTime, String service) {
        this.customerName = customerName;
        this.stylistName = stylistName;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.service = service;
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getStylistName() { return stylistName; }
    public void setStylistName(String stylistName) { this.stylistName = stylistName; }

    public String getSlotDate() { return slotDate; }
    public void setSlotDate(String slotDate) { this.slotDate = slotDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
}
