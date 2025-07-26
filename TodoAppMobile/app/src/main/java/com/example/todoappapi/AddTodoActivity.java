package com.example.todoappapi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTodoActivity extends AppCompatActivity {

    private EditText editTitle;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        editTitle = findViewById(R.id.editTextTitle);
        btnSave = findViewById(R.id.buttonSave);

        btnSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Lütfen bir başlık giriniz", Toast.LENGTH_SHORT).show();
                return;
            }

            // Yeni Todo nesnesi oluştur
            Todo newTodo = new Todo();
            newTodo.setTitle(title);
            newTodo.setCompleted(false); // Yeni görev tamamlanmamış olarak başlar

            // API çağrısı
            TodoApiServiceDAO apiService = ApiClient.getRetrofitInstance().create(TodoApiServiceDAO.class);
            Call<Todo> call = apiService.createTodo(newTodo); // <-- BURADA DÖNÜŞ Call<Todo>

            call.enqueue(new Callback<Todo>() {
                @Override
                public void onResponse(Call<Todo> call, Response<Todo> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(AddTodoActivity.this, "Görev başarıyla eklendi", Toast.LENGTH_SHORT).show();
                        finish(); // Sayfayı kapat
                    } else {
                        Toast.makeText(AddTodoActivity.this, "Ekleme başarısız! Kod: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Todo> call, Throwable t) {
                    Toast.makeText(AddTodoActivity.this, "Hata: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
