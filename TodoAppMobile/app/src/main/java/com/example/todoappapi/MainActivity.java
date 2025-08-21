package com.example.todoappapi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private boolean showOnlyPending = false;
    private List<Todo> fullTodoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔐 Bildirim izni kontrolü
        checkNotificationPermission();

        // 🚀 Firebase token'ı al ve sunucuya gönder
        FirebaseTokenSender.sendTokenToServer();

        initViews();
        loadTodos();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(null);
        recyclerView.setAdapter(todoAdapter);
        swipeRefresh.setOnRefreshListener(this::loadTodos);

        // ➕ Görev ekleme
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddTodoActivity.class));
        });

        // 🔄 Filtreleme
        TextView textFilter = findViewById(R.id.textFilter);
        textFilter.setOnClickListener(v -> {
            showOnlyPending = !showOnlyPending;
            textFilter.setText(showOnlyPending ? "Tüm görevleri göster" : "Sadece yapılacakları göster");
            applyFilter();
        });

        attachSwipeToDelete();
    }

    private void loadTodos() {
        TodoApiServiceDAO apiService = ApiClient.getRetrofitInstance().create(TodoApiServiceDAO.class);
        apiService.getTodos().enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullTodoList = response.body();
                    applyFilter();
                } else {
                    Log.e("TODO_ERROR", "Kod: " + response.code());
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e("TODO_FAIL", "Hata: " + t.getMessage());
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void applyFilter() {
        if (showOnlyPending) {
            List<Todo> pendingList = new ArrayList<>();
            for (Todo todo : fullTodoList) {
                if (!todo.isCompleted()) {
                    pendingList.add(todo);
                }
            }
            todoAdapter.setTodoList(pendingList);
        } else {
            todoAdapter.setTodoList(fullTodoList);
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void attachSwipeToDelete() {
        // Swipe-to-delete fonksiyonu burada olacaksa ekle.
    }
}
