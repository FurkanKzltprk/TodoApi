package com.example.todoappapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


// furkan: Todo nesneleri için API erişim ve DAO (veri erişim katmanı) görevi görür.
// furkan: Bu arayüz, Retrofit ile REST API çağrılarının tanımlandığı yerdir.

public interface TodoApiServiceDAO {


    @POST("api/todoitems/getall")
    Call<List<Todo>> getTodos();



    @POST("api/todoitems/create")
    Call<Todo> createTodo(@Body Todo todo);

    @POST("api/todoitems/update")
    Call<Void> updateTodo(@Body Todo todo);

    @POST("api/todoitems/delete")
    Call<Void> deleteTodo(@Body Todo todo);

    // ID ile getir (şimdilik kullanmıyoruz ama ekleyebiliriz)
    @POST("api/todoitems/getbyid")
    Call<Todo> getTodoById(@Body int id);

    // 5. ID'YE GÖRE TODO GETİR (Swagger'da vardı ama henüz eklememişsin, istersen bunu da ekleyebilirim)
}






//furkan: dikkat et hepsi POST isteği !!! //red:aman dikkat!!! GET,PUT,DELETE yok sadece POST (security)
/*
        Açıklamalar:
        @POST("todoitems/getall") → REST API'de tüm verileri getiren POST endpoint.
        @POST("todoitems/add")   → Yeni bir todo öğesi eklemek için kullanılacak POST endpoint.
        @Body → İstek gövdesinde (body) gönderilecek nesneyi belirtir.
        Call<T> → Retrofit’in HTTP isteğini temsil eder. Async/sync çağrılarda kullanılır.
    */