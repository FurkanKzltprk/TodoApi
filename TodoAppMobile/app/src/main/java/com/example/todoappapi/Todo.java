package com.example.todoappapi;

import com.google.gson.annotations.SerializedName;

public class Todo {

    private int id;
    private String title;

    @SerializedName("isCompleted")  //furkan:bu satır çok önemli !! api ile uyumlu olmalı !!
    private boolean isCompleted;

    public Todo() {}

    public Todo(int id, String title, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.isCompleted = isCompleted;
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
}
