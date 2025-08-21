package com.example.todoappapi;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.titleTextView.setText(todo.getTitle());

        if (todo.getCreatedAt() != null) {
            holder.dateTextView.setText(todo.getCreatedAt().substring(0, 10));
        } else {
            holder.dateTextView.setText("-");
        }

        holder.completedCheckBox.setOnCheckedChangeListener(null);
        holder.completedCheckBox.setChecked(todo.isCompleted());

        holder.completedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (todo.isCompleted() == isChecked) return;

            todo.setCompleted(isChecked);
            TodoApiServiceDAO apiService = ApiClient.getRetrofitInstance().create(TodoApiServiceDAO.class);
            Call<Void> call = apiService.updateTodo(todo);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(holder.itemView.getContext(), "Güncellendi ✅", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "Hata: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.itemView.setOnLongClickListener(v -> {
            EditText editText = new EditText(holder.itemView.getContext());
            editText.setText(todo.getTitle());

            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Görevi Güncelle")
                    .setView(editText)
                    .setPositiveButton("Kaydet", (dialog, which) -> {
                        String updatedTitle = editText.getText().toString().trim();
                        if (!updatedTitle.isEmpty()) {
                            todo.setTitle(updatedTitle);
                            TodoApiServiceDAO apiService = ApiClient.getRetrofitInstance().create(TodoApiServiceDAO.class);
                            Call<Void> call = apiService.updateTodo(todo);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        notifyItemChanged(holder.getAdapterPosition());
                                        Toast.makeText(holder.itemView.getContext(), "Güncellendi ✅", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(holder.itemView.getContext(), "Sunucu hatası ❌", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(holder.itemView.getContext(), "Hata: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("İptal", null)
                    .show();

            return true;
        });

        // ✅ Silme butonu
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Silmek istediğinize emin misiniz?")
                    .setMessage("Bu görev kalıcı olarak silinecek.")
                    .setPositiveButton("Evet", (dialog, which) -> {
                        TodoApiServiceDAO apiService = ApiClient.getRetrofitInstance().create(TodoApiServiceDAO.class);
                        Call<Void> call = apiService.deleteTodo(todo);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    int pos = holder.getAdapterPosition();
                                    todoList.remove(pos);
                                    notifyItemRemoved(pos);
                                    Toast.makeText(holder.itemView.getContext(), "Silindi ✅", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(holder.itemView.getContext(), "Sunucu hatası ❌", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(holder.itemView.getContext(), "Bağlantı hatası 😞", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Hayır", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return (todoList != null) ? todoList.size() : 0;
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, dateTextView;
        CheckBox completedCheckBox;
        Button deleteButton;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.todo_title);
            dateTextView = itemView.findViewById(R.id.todo_date);
            completedCheckBox = itemView.findViewById(R.id.todo_checkbox);
            deleteButton = itemView.findViewById(R.id.delete_button); // 👈 Sil butonu tanımı
        }
    }
}
