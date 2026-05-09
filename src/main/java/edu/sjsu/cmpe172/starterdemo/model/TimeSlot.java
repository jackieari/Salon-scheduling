package edu.sjsu.cmpe172.starterdemo.model;

public class TimeSlot {

    private Long slotId;
    private String stylistName;
    private String slotDate;
    private String startTime;
    private String service;
    private boolean isBlocked;

    public TimeSlot() {}

    public TimeSlot(Long slotId, String stylistName, String slotDate, String startTime, String service) {
        this.slotId = slotId;
        this.stylistName = stylistName;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.service = service;
        this.isBlocked = false;
    }

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

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean isBlocked) { this.isBlocked = isBlocked; }
}
