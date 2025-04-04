package com.pro.api_gateway.dto;

import java.util.List;

public class Application {

    private String name;
    private List<Instance> instance;

    public Application(String name, List<Instance> instance) {
        this.name = name;
        this.instance = instance;
    }

    public Application() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Instance> getInstance() {
        return instance;
    }

    public void setInstance(List<Instance> instance) {
        this.instance = instance;
    }

}
