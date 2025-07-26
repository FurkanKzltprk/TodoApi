package com.example.todoappapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(null);
        recyclerView.setAdapter(todoAdapter);

        // Swipe yenileme
        swipeRefresh.setOnRefreshListener(this::loadTodos);

        // FAB ile yeni görev ekleme
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
            startActivity(intent);
        });

        // Uygulama açılır açılmaz verileri getir
        loadTodos();
    }

    private void loadTodos() {
        TodoApiServiceDAO apiService = ApiClient.getRetrofitInstance().create(TodoApiServiceDAO.class);
        Call<List<Todo>> call = apiService.getTodos();

        call.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    todoAdapter.setTodoList(response.body());
                    Log.d("TODO_SUCCESS", "Veriler başarıyla alındı");
                } else {
                    Log.e("TODO_ERROR", "Response code: " + response.code());
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e("TODO_FAIL", "Error: " + t.getMessage());
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}
