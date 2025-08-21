package com.example.todoappapi;

public class TokenRequest {
    private String token;
    private String deviceModel;
    private String manufacturer;
    private String platform;
    private String appVersion;

    public TokenRequest(String   token, String deviceModel, String manufacturer, String platform, String appVersion) {
        this.token = token;
        this.deviceModel = deviceModel;
        this.manufacturer = manufacturer;
        this.platform = platform;
        this.appVersion = appVersion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
