package com.google.mlkit.vision.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class botton_nav extends AppCompatActivity {
    BottomNavigationView bnv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botton_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameContainer,new HomeFragment()).commit();
        getSupportActionBar().hide();
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
}