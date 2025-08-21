package com.example.todoappapi;

import android.os.Build;
import android.util.Log;

import com.google.android.datatransport.backend.cct.BuildConfig;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseTokenSender {

    public static void sendTokenToServer() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("FCM_TOKEN", "Token alınamadı", task.getException());
                return;
            }

            String token = task.getResult();
            Log.d("FCM_TOKEN", "Token: " + token);

            // Ek cihaz bilgileri
            String deviceModel = Build.MODEL;
            String manufacturer = Build.MANUFACTURER;
            String platform = "Android";
            String appVersion = BuildConfig.VERSION_NAME;

            // TokenRequest nesnesi oluşturuluyor
            TokenRequest tokenRequest = new TokenRequest(token, deviceModel, manufacturer, platform, appVersion);

            // API isteği gönderiliyor
            TokenApiService apiService = ApiClient.getRetrofitInstance().create(TokenApiService.class);
            Call<Void> call = apiService.sendToken(tokenRequest);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("TOKEN_GÖNDERİLDİ", "Token başarıyla kaydedildi");
                    } else {
                        Log.e("TOKEN_HATA", "Sunucu hatası: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("TOKEN_HATA", "Bağlantı hatası: " + t.getMessage());
                }
            });
        });
    }
}
