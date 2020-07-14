package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);

        TextView profileTitle = findViewById(R.id.titleTextForProfile);
        TextView userName_textView = findViewById(R.id.name_textView);
        TextView userEmail_textView = findViewById(R.id.email_textView);

        String userName = getIntent().getStringExtra("USER_NAME");
        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        profileTitle.setText(userName);
        userName_textView.setText(userName);
        userEmail_textView.setText(userEmail);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        initialBottomNavigationMenu(bottomNavigationView);
    }
    private void initialBottomNavigationMenu(BottomNavigationView bottomNavigationView) {
        //bottomNavigationView.setSelectedItemId(R.id.chats);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add:
                    case R.id.profile:
                        break;
                    case R.id.chats:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });
    }
}
