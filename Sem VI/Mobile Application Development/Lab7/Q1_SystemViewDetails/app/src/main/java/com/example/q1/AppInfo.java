package com.example.q1;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppInfo implements Serializable {
    private String appName;
    private String packageName;
    private transient Drawable icon;
    private String version;
    private boolean isSystemApp;
    private long size;

    public AppInfo(String appName, String packageName, Drawable icon, String version, boolean isSystemApp, long size) {
        this.appName = appName;
        this.packageName = packageName;
        this.icon = icon;
        this.version = version;
        this.isSystemApp = isSystemApp;
        this.size = size;
    }

    public String getAppName() { return appName; }
    public String getPackageName() { return packageName; }
    public Drawable getIcon() { return icon; }
    public String getVersion() { return version; }
    public boolean isSystemApp() { return isSystemApp; }
    public long getSize() { return size; }
}
