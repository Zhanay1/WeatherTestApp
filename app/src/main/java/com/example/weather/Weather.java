package com.example.weather;

public class Weather {
    private int id;
    private String name;
    private String temp;
    private String description;

    public Weather(int id, String name, String temp, String description) {
        this.id = id;
        this.name = name;
        this.temp = temp;
        this.description = description;
    }
    public Weather(String name, String temp, String description) {
        this.name = name;
        this.temp = temp;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
