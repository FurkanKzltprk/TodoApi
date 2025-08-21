package com.example.todoappapi;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://192.168.1.109:7272/";  // kendi IP adresin ve portun
    //furkan: Soru ?? burada ip bilgisayar açılıp kapanınca değişti burada da sürekli değiştirmek zorunda mı kalıcam ? Cevap : EVET böylesi daha iyi .
    //uyari:  /res/xml/network_security_config.xml de de değişim olacak.
    private static Retrofit retrofit;
   //uyari:selam bu bir uyarı mesajıdır.
    //dikkat:selam bu daha önemli bir uyarı mesajıdır


    //blue: selam bunlar renkli yorum satırları.

    //furkan: selam burada özel bir yorum satırı renklendirmesi ayarı yaptım.
    //furkan: bu yorum satırı özelleştirmesiyle proje genelinde açıklayıcı yorum satıları ekleyeceğim.

    //furkan:DOAIMP yok ,retrofit kendi içerisinde onu hallediyor !!.
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {  //furkan: retrofit nesnesini bir defa oluşturdu,ikinci çağrıda yeni nesne oluşmaz,aynısı döner , bu yapı singletondur işte .

            // HTTP istek/cevap logları için interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // OkHttp client eklendi
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
