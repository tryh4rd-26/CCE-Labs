package com.example.q1;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);

        String appName = getIntent().getStringExtra("appName");
        String packageName = getIntent().getStringExtra("packageName");
        String version = getIntent().getStringExtra("version");
        long size = getIntent().getLongExtra("size", 0);

        TextView tvName = findViewById(R.id.detailAppName);
        TextView tvPackage = findViewById(R.id.detailPackageName);
        TextView tvVersion = findViewById(R.id.detailVersion);
        TextView tvSize = findViewById(R.id.detailSize);
        TextView tvPermissionsList = findViewById(R.id.detailPermissionsList);

        tvName.setText(appName);
        tvPackage.setText("Package: " + packageName);
        tvVersion.setText("Version: " + version);
        tvSize.setText("Size: " + (size / 1024 / 1024) + " MB");

        tvPermissionsList.setText(getAllPermissions(packageName));
    }

    private String getAllPermissions(String packageName) {
        StringBuilder sb = new StringBuilder();
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            if (requestedPermissions != null) {
                for (String p : requestedPermissions) {
                    sb.append(p).append("\n");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return sb.length() == 0 ? "No permissions requested" : sb.toString();
    }
}
