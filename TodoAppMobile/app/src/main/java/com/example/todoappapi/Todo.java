package com.example.todoappapi;

import com.google.gson.annotations.SerializedName;

public class Todo {

    private int id;
    private String title;

    @SerializedName("isCompleted") // API'deki C# modeliyle uyumlu
    private boolean isCompleted;

    @SerializedName("createdAt")   // API'den gelen timestamp için
    private String createdAt;

    public Todo() {}

    public Todo(int id, String title, boolean isCompleted, String createdAt) {
        this.id = id;
        this.title = title;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
    }

    // Getter ve Setter'lar
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
