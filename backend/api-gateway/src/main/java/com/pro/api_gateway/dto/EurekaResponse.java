package com.pro.api_gateway.dto;

public class EurekaResponse {

    private Applications applications;

    public EurekaResponse(Applications applications) {
        this.applications = applications;
    }

    public Applications getApplications() {
        return applications;
    }

    public void setApplications(Applications applications) {
        this.applications = applications;
    }
}
