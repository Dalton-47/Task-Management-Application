package com.example.luna;

public class Task_Class {

    String title;
    String description;
    String startTime;
    String dueDate;
    String category;

    public Task_Class(String title, String description, String startTime, String dueDate, String category) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.dueDate = dueDate;
        this.category = category;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
