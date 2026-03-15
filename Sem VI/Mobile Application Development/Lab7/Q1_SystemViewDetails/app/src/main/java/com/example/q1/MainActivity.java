package com.example.q1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppAdapter adapter;
    private List<AppInfo> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appList = getInstalledApps();
        adapter = new AppAdapter(appList, this::showAppOptions);
        recyclerView.setAdapter(adapter);
    }

    private List<AppInfo> getInstalledApps() {
        List<AppInfo> apps = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            try {
                PackageInfo pInfo = pm.getPackageInfo(packageInfo.packageName, 0);
                boolean isSystem = (packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
                long size = new File(packageInfo.sourceDir).length();
                apps.add(new AppInfo(
                        pm.getApplicationLabel(packageInfo).toString(),
                        packageInfo.packageName,
                        pm.getApplicationIcon(packageInfo),
                        pInfo.versionName,
                        isSystem,
                        size
                ));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return apps;
    }

    private void showAppOptions(AppInfo app) {
        String type = app.isSystemApp() ? "System App" : "User-Installed App";
        String permissions = getSpecialPermissions(app.getPackageName());

        String[] options = {"Open App", "Uninstall", "View Details"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(app.getAppName() + " (" + type + ")\nPermissions: " + permissions)
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            openApp(app.getPackageName());
                            break;
                        case 1:
                            confirmUninstall(app.getPackageName());
                            break;
                        case 2:
                            viewDetails(app);
                            break;
                    }
                })
                .show();
    }

    private String getSpecialPermissions(String packageName) {
        StringBuilder sb = new StringBuilder();
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            if (requestedPermissions != null) {
                boolean hasLocation = false;
                boolean hasCamera = false;
                for (String p : requestedPermissions) {
                    if (p.contains("LOCATION")) hasLocation = true;
                    if (p.contains("CAMERA")) hasCamera = true;
                }
                if (hasLocation) sb.append("Location ");
                if (hasCamera) sb.append("Camera");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return sb.length() == 0 ? "None" : sb.toString();
    }

    private void openApp(String packageName) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            Toast.makeText(this, "Cannot open this app", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmUninstall(String packageName) {
        new AlertDialog.Builder(this)
                .setTitle("Uninstall")
                .setMessage("Are you sure you want to uninstall " + packageName + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void viewDetails(AppInfo app) {
        Intent intent = new Intent(this, AppDetailsActivity.class);
        intent.putExtra("appName", app.getAppName());
        intent.putExtra("packageName", app.getPackageName());
        intent.putExtra("version", app.getVersion());
        intent.putExtra("size", app.getSize());
        startActivity(intent);
    }
}
