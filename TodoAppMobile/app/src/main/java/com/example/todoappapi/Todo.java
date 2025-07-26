package com.example.todoappapi;

public class Todo {
    private int id;
    private String title;
    private boolean isCompleted;

    public Todo() {}

    // boş constructor
/* Neden 2 tane constructor var ???
1- JSON parse işlemlerinde (Retrofit, Gson, Jackson gibi kütüphaneler), Java sınıfını
oluşturabilmek için önce boş bir nesne yaratırlar, sonra setter metodlarıyla alanları doldururlar.

2-Android sisteminin veya
 bazı framework’lerin reflection kullanarak nesne yaratabilmesi için parametresiz constructor gerekir.


3-Ayrıca bazen elle manuel olarak boş nesne yaratmak isteyebilirsin:
*/


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
