package com.google.mlkit.vision.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.mlkit.vision.demo.java.NetworkChangeReceiver;

public class botton_nav extends AppCompatActivity {
    BottomNavigationView bnv;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botton_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameContainer,new HomeFragment()).commit();
        getSupportActionBar().hide();
        broadcastReceiver=new NetworkChangeReceiver();
        registerNetworkBroadcastReceiver();

        bnv=findViewById(R.id.bottomNavigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment temp=null;

                switch(item.getItemId()) {
                    case R.id.menu_home:
                        temp = new HomeFragment();
                        break;
                    case R.id.menu_live:
                        temp = new LiveFragment();
                        break;
                    case R.id.menu_profile:
                        temp = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameContainer,temp).commit();

                return true;
            }
        });
    }

    private void registerNetworkBroadcastReceiver() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    private void unregisterNetwork(){
        try{
        unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }
}