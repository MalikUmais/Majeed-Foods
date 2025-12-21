package models;

public class Shift {
    private int shiftId;
    private String shiftName;
    private String shiftTiming;
    private String shiftDays;
    private int waiterId;

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getShiftTiming() {
        return shiftTiming;
    }

    public void setShiftTiming(String shiftTiming) {
        this.shiftTiming = shiftTiming;
    }

    public String getShiftDays() {
        return shiftDays;
    }

    public void setShiftDays(String shiftDays) {
        this.shiftDays = shiftDays;
    }

    public int getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(int waiterId) {
        this.waiterId = waiterId;
    }
}
