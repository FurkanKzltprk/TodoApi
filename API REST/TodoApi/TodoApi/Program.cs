using TodoApi.Data;
using Microsoft.EntityFrameworkCore;
using TodoApi.Services;
using System.Text.Json.Serialization;

var builder = WebApplication.CreateBuilder(args);

// 📦 DB Context
builder.Services.AddDbContext<TodoDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

// 📦 Controller + JSON ayarları (Swagger'ın döngü hatasını önlemek için)
builder.Services.AddControllers()
    .AddJsonOptions(options =>
    {
        options.JsonSerializerOptions.ReferenceHandler = ReferenceHandler.IgnoreCycles;
        options.JsonSerializerOptions.WriteIndented = true;
    });

// 📦 Swagger
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// 📦 HTTP Client ve Servisler
builder.Services.AddHttpClient();
builder.Services.AddScoped<FirebaseNotificationService>();

// 📦 CORS
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(policy =>
    {
        policy.AllowAnyOrigin()
              .AllowAnyMethod()
              .AllowAnyHeader();
    });
});

var app = builder.Build();

// 🔧 Middleware
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseCors();

// Https devre dışı bırakmak istersen aşağıdaki satırı yorum satırına alabilirsin
// app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
