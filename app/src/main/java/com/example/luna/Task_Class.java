package com.example.luna;

public class Task_Class {

    String title;
    String description;
    String startTimme;
    String dueDate;
    String category;

    public Task_Class(String title, String description, String startTimme, String dueDate, String category) {
        this.title = title;
        this.description = description;
        this.startTimme = startTimme;
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

    public String getStartTimme() {
        return startTimme;
    }

    public void setStartTimme(String startTimme) {
        this.startTimme = startTimme;
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
