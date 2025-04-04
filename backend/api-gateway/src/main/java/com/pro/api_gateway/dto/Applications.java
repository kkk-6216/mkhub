package com.pro.api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Applications {

    @JsonProperty("versions__delta")
    private String versionsDelta;

    @JsonProperty("apps__hashcode")
    private String appsHashcode;
    private List<Application> application;

    public Applications(String versionsDelta, String appsHashcode, List<Application> application) {
        this.versionsDelta = versionsDelta;
        this.appsHashcode = appsHashcode;
        this.application = application;
    }

    public String getVersionsDelta() {
        return versionsDelta;
    }

    public void setVersionsDelta(String versionsDelta) {
        this.versionsDelta = versionsDelta;
    }

    public String getAppsHashcode() {
        return appsHashcode;
    }

    public void setAppsHashcode(String appsHashcode) {
        this.appsHashcode = appsHashcode;
    }

    public List<Application> getApplication() {
        return application;
    }

    public void setApplication(List<Application> application) {
        this.application = application;
    }
}
