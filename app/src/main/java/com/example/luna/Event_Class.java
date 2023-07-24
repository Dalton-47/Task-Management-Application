package com.example.luna;

import java.io.Serializable;

public class Event_Class implements Serializable {
    String title;
    String description;
    String dueDate;
    String startTime;
    String endTime;
    String category;

    String location;

    public Event_Class(String title, String description, String dueDate, String startTime, String endTime, String category, String location) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.location = location;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
