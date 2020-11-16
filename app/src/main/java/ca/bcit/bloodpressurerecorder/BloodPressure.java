package ca.bcit.bloodpressurerecorder;

import java.util.Date;

public class BloodPressure {
    private String bloodPressureId;
    private String systolicReading;
    private String diastolicReading;
    private String familyMember;
    private Date dateTime;
    private String condition;

    public BloodPressure() { }

    public BloodPressure(String bloodPressureId, String systolicReading, String diastolicReading, String familyMember, Date dateTime, String condition) {
        this.bloodPressureId = bloodPressureId;
        this.systolicReading = systolicReading;
        this.diastolicReading = diastolicReading;
        this.familyMember = familyMember;
        this.dateTime = dateTime;
        this.condition = condition;
    }



    public String getBloodPressureId() {
        return bloodPressureId;
    }

    public void setBloodPressureId(String bloodPressureId) {
        this.bloodPressureId = bloodPressureId;
    }

    public String getSystolicReading() {
        return systolicReading;
    }

    public void setSystolicReading(String systolicReading) {
        this.systolicReading = systolicReading;
    }

    public String getDiastolicReading() {
        return diastolicReading;
    }

    public void setDiastolicReading(String diastolicReading) {
        this.diastolicReading = diastolicReading;
    }

    public String getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(String familyMember) {
        this.familyMember = familyMember;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
