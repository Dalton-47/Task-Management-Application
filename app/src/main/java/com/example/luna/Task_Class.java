package com.example.luna;

public class Task_Class {

    String title;
    String description;
    String dueTime;
    String dueDate;
    String category;
    String status;

    String dateTime;

    public Task_Class() {
    }


    public Task_Class(String title, String description, String dueTime, String dueDate, String category, String status, String dateTime) {
        this.title = title;
        this.description = description;
        this.dueTime = dueTime;
        this.dueDate = dueDate;
        this.category = category;
        this.status = status;
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
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
