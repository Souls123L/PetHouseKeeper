package com.example.pethousekeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1; // 请求码
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 初始化 DrawerLayout 和 NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 设置 ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 创建通知渠道
        createNotificationChannel();

        // 检查并请求 POST_NOTIFICATIONS 权限
        checkAndRequestNotificationPermission();

        // 加载默认的 Fragment（如果没有保存状态）
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // 处理导航到特定 Fragment 的 Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("navigate_to")) {
            String target = intent.getStringExtra("navigate_to");
            if ("AlarmFragment".equals(target)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AlarmFragment())
                        .commit();
                navigationView.setCheckedItem(R.id.Alarm); // 确保导航菜单项 ID 正确
            }
            // 添加更多的導航條件（如果有其他 Fragment）
        }
    }

    /**
     * 创建通知渠道
     */
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notification Channel";
            String desc = "Channel for Alarm Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(desc);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.POST_NOTIFICATIONS)) {
                    Toast.makeText(this, "需要通知权限以显示提醒通知。",
                            Toast.LENGTH_LONG).show();
                }
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_CODE_POST_NOTIFICATIONS);
            }
        }
    }

    /**
     * 处理权限请求的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "通知权限已授予。",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "通知权限被拒绝。您将无法接收提醒通知。",
                        Toast.LENGTH_LONG).show();
                // 可选择进一步提示用户，解释为什么需要此权限，并在适当的时候再次请求
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (itemId == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (itemId == R.id.nav_setting) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new settingFragment()).commit();
        } else if (itemId == R.id.Gallery) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new GalleryFragment()).commit();
        } else if (itemId == R.id.Calculator) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CalculatorFragment()).commit();
        } else if (itemId == R.id.Alarm) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AlarmFragment()).commit();
        } else if (itemId == R.id.Notes) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NotesFragment()).commit();
        } else if (itemId == R.id.Maps) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MapFragment()).commit();
        } else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // 检查当前是否有 Fragment 在回退栈中
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
}