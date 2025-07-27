package com.example.todoappapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://192.168.1.104:7272/";  // kendi IP adresin ve portun
    //furkan: Soru ?? burada ip bilgisayar açılıp kapanınca değişti burada da sürekli değiştirmek zorunda mı kalıcam ? Cevap : EVET böylesi daha iyi .
    private static Retrofit retrofit;


    //red: DOAIMP yok ,retrofit kendisi yapıyor.
    //blue: selam bunlar renkli yorum satırları.

    //furkan: selam burada özel bir yorum satırı renklendirmesi ayarı yaptım.
    //furkan: bu yorum satırı özelleştirmesiyle proje genelinde açıklayıcı yorum satıları ekleyeceğim.


    //furkan:DOAIMP yok ,retrofit kendi içerisinde onu hallediyor !!.
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {  //furkan: retrofit nesnesini bir defa oluşturdu,ikinci çağrıda yeni nesne oluşmaz,aynısı döner , bu yapı singletondur işte .
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
