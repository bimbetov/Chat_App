package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener, DataAdapter.OnChatListener {
    private static int SIGN_IN_CODE = 1;
    private RelativeLayout rooms_activity;
    private List<Room> rooms = new ArrayList<>();
    private FloatingActionButton createRoomBtn;
    private RecyclerView.Adapter mAdapter;
    private DatabaseReference myRefForList;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(rooms_activity, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
                //displayAllMessages();
            } else {
                Snackbar.make(rooms_activity, "Вы не авторизованы", Snackbar.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        //Проверка на авторизованность
        signCheck();

        myRefForList = FirebaseDatabase.getInstance().getReference("rooms");

        createRoomBtn = findViewById(R.id.createBtn);
        createRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new DataAdapter(rooms, this);
        recyclerView.setAdapter(mAdapter);

        updateList();
    }

    private void updateList(){
        myRefForList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                rooms.add(dataSnapshot.getValue(Room.class));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void signCheck() {
        rooms_activity = findViewById(R.id.rooms_activity);
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        else {
            Snackbar.make(rooms_activity, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
        }
    }

    public void showDialog() {
        ExampleDialog dialog = new ExampleDialog();
        dialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(String chatName) {
        setInitialData(chatName);
    }

    public void setInitialData(String newChatName) {
        Room room = new Room(newChatName);
        myRefForList.push().setValue(room);
    }

    @Override
    public void onChatClick(int position) {
        Intent intent = new Intent(this, DisplayAllMessagesInTheRoom.class);
        intent.putExtra("CHAT_NAME", rooms.get(position).getChatName());
        startActivity(intent);
    }
}
