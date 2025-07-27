from docx import Document
from docx.shared import Pt
from docx.enum.text import WD_PARAGRAPH_ALIGNMENT

# Markdown içeriği
markdown_content = """
📝 Todo List Uygulaması – Backend (ASP.NET Core Web API) + Android Client (Java)

🇹🇷 Türkçe Açıklama
Bu proje, yapılacaklar listesi (Todo List) yönetimi için geliştirilmiş bir ASP.NET Core Web API ve Android (Java) tabanlı istemci uygulamasıdır.  
Veri katmanında Entity Framework Core ve SQL Server kullanılmıştır. Kullanıcılar görevleri ekleyebilir, güncelleyebilir, listeleyebilir ve silebilir.

Mobil istemci, Android Studio üzerinde Java dili ile geliştirilmiştir. Retrofit kullanılarak REST API ile haberleşmektedir.
Projenin ilerleyen aşamalarında Flutter istemci ile de desteklenecektir.

🔧 Kullanılan Teknolojiler

Backend (Web API)
- ASP.NET Core 8 Web API
- Entity Framework Core
- SQL Server
- Swagger (API Dokümantasyonu)
- Postman (API Testi)

Android (Mobil Uygulama)
- Java (Android SDK)
- Retrofit2 (HTTP istemcisi)
- RecyclerView + SwipeRefreshLayout
- Material Design bileşenleri

🌐 English Description
This project is a Todo List management system consisting of an ASP.NET Core Web API backend and an Android client written in Java.  
The API supports full CRUD operations via REST, backed by Entity Framework Core and SQL Server.

The mobile client uses Retrofit to communicate with the Web API and features a modern UI built with RecyclerView and Material Design.
In the future, a Flutter client will also be supported.

Technologies Used

Backend
- ASP.NET Core 8 Web API
- Entity Framework Core
- SQL Server
- Swagger (API Documentation)
- Postman (API Testing)

Android Client
- Java (Android)
- Retrofit2
- RecyclerView
- SwipeRefreshLayout
- Material Design

📂 Proje Yapısı (Project Structure)

ToDoProject/
├── WebAPI/
│   ├── Controllers/
│   ├── Models/
│   ├── Data/
│   ├── Program.cs
│   ├── Startup.cs
│   └── ToDoAPI.csproj
│
├── AndroidApp/
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/example/todoappapi/
│   │   │   ├── res/layout/
│   │   │   └── AndroidManifest.xml
│   └── build.gradle
│
└── README.md

🚀 Başlangıç (How to Run)

Web API (ASP.NET Core)
1. appsettings.json dosyasına SQL Server bağlantı cümleni gir.
2. Komut satırından:
   dotnet ef database update
   dotnet run
3. https://localhost:5001/swagger adresi üzerinden API endpoint’lerini test et.

Android Uygulama (Java)
1. Android Studio ile AndroidApp/ klasörünü aç.
2. ApiClient.java içinde BASE_URL değişkenine kendi bilgisayar IP adresini gir.
3. Uygulamayı çalıştır → Görevleri listele, ekle, tamamla.

Mevcut Özellikler
- Görevleri listeleme
- Görev ekleme
- Görev güncelleme (tamamlandı checkbox)
- Görev silme (isteğe bağlı)
- Veritabanı senkronizasyonu
- Swipe-to-refresh desteği
- Material Design arayüz

🛠 Planlanan Geliştirmeler
- Görevleri kategoriye ayırma
- Göreve tarih/saat atama
- Firebase Authentication entegrasyonu
- Flutter istemci geliştirme
- Dark mode desteği
- Notification sistemi

📄 Lisans
Bu proje MIT Lisansı ile lisanslanmıştır. Detaylar için LICENSE dosyasına göz atabilirsiniz.

🤝 Katkı Sağlamak
Katkıda bulunmak isterseniz pull request gönderebilir, issue açabilir veya projeyi fork ederek geliştirmeye katkı sağlayabilirsiniz.

✉️ İletişim
Proje geliştiricisi: Furkan
E-posta: furkan@example.com
LinkedIn: https://linkedin.com/in/furkan-developer
"""

# Word belgesini oluştur
doc = Document()
doc.add_heading("README – Todo List Uygulaması", 0)

# Her satırı ekle
for line in markdown_content.strip().split("\n"):
    para = doc.add_paragraph(line.strip())
    para.paragraph_format.space_after = Pt(4)
    para.alignment = WD_PARAGRAPH_ALIGNMENT.LEFT

# Kaydet
output_path = "/mnt/data/TodoProject_README.docx"
doc.save(output_path)
output_path
