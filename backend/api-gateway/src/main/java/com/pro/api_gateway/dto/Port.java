package com.pro.api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Port {

    @JsonProperty("$")
    private int port;

    @JsonProperty("@enabled")
    private String enabled;

    public Port(int port, String enabled) {
        this.port = port;
        this.enabled = enabled;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

}
