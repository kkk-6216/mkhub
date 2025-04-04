package com.pro.api_gateway.dto;

public class LeaseInfo {

    private int renewalIntervalInSecs;
    private int durationInSecs;
    private long registrationTimestamp;
    private long lastRenewalTimestamp;
    private long evictionTimestamp;
    private long serviceUpTimestamp;

    public LeaseInfo(int renewalIntervalInSecs, int durationInSecs, long registrationTimestamp, long lastRenewalTimestamp, long evictionTimestamp, long serviceUpTimestamp) {
        this.renewalIntervalInSecs = renewalIntervalInSecs;
        this.durationInSecs = durationInSecs;
        this.registrationTimestamp = registrationTimestamp;
        this.lastRenewalTimestamp = lastRenewalTimestamp;
        this.evictionTimestamp = evictionTimestamp;
        this.serviceUpTimestamp = serviceUpTimestamp;
    }

    public int getRenewalIntervalInSecs() {
        return renewalIntervalInSecs;
    }

    public void setRenewalIntervalInSecs(int renewalIntervalInSecs) {
        this.renewalIntervalInSecs = renewalIntervalInSecs;
    }

    public int getDurationInSecs() {
        return durationInSecs;
    }

    public void setDurationInSecs(int durationInSecs) {
        this.durationInSecs = durationInSecs;
    }

    public long getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    public void setRegistrationTimestamp(long registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp;
    }

    public long getLastRenewalTimestamp() {
        return lastRenewalTimestamp;
    }

    public void setLastRenewalTimestamp(long lastRenewalTimestamp) {
        this.lastRenewalTimestamp = lastRenewalTimestamp;
    }

    public long getEvictionTimestamp() {
        return evictionTimestamp;
    }

    public void setEvictionTimestamp(long evictionTimestamp) {
        this.evictionTimestamp = evictionTimestamp;
    }

    public long getServiceUpTimestamp() {
        return serviceUpTimestamp;
    }

    public void setServiceUpTimestamp(long serviceUpTimestamp) {
        this.serviceUpTimestamp = serviceUpTimestamp;
    }
}
