package com.example.studenttaskmanager;

public class Task {
    public int id;
    public String title, description, dueDate;
    public boolean completed;

    public Task(int id, String title, String description,
                String dueDate, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
    }
}