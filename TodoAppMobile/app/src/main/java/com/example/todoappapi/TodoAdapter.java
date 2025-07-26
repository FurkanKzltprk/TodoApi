package com.example.todoappapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<Todo> todoList;

    public TodoAdapter(List<Todo> todoList) {
        this.todoList = todoList;
    }

    public void setTodoList(List<Todo> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.titleTextView.setText(todo.getTitle());
        holder.completedCheckBox.setChecked(todo.isCompleted());

        // Checkbox değişimi dinleniyor
        holder.completedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setCompleted(isChecked);

            TodoApiServiceDAO apiService = ApiClient.getRetrofitInstance().create(TodoApiServiceDAO.class);
            Call<Todo> call = apiService.updateTodo(todo);

            call.enqueue(new Callback<Todo>() {
                @Override
                public void onResponse(Call<Todo> call, Response<Todo> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(holder.itemView.getContext(), "Güncellendi ✅", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Güncelleme başarısız ❌", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Todo> call, Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "Bağlantı hatası 😞", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return todoList != null ? todoList.size() : 0;
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        CheckBox completedCheckBox;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.todo_title);
            completedCheckBox = itemView.findViewById(R.id.todo_checkbox);
        }
    }
}
