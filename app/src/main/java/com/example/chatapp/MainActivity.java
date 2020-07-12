package com.example.chatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener, DataAdapter.OnChatListener {
    private static int SIGN_IN_CODE = 1;
    private RelativeLayout rooms_activity;
    private List<Room> rooms = new ArrayList<>();
    private FloatingActionButton createRoomBtn;
    RecyclerView.Adapter mAdapter;
    DatabaseReference myRef;

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

        myRef = FirebaseDatabase.getInstance().getReference("rooms");
        rooms_activity = findViewById(R.id.rooms_activity);

        //Пользователь еще не авторизован
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        else {
            Snackbar.make(rooms_activity, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
        }

        RecyclerView recyclerView = findViewById(R.id.list);

        createRoomBtn = findViewById(R.id.createBtn);
        createRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //rooms.add(0, new Room("")); //Добавляем пустой итем в 0 позицию для того чтобы новые итемы записывались под него
        mAdapter = new DataAdapter(rooms, this);
        recyclerView.setAdapter(mAdapter);
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
        rooms.add(0, room);
        mAdapter.notifyItemInserted(0);
        myRef.push().setValue(room);
    }

    @Override
    public void onChatClick(int position) {
        Intent intent = new Intent(this, DisplayAllMessagesInTheRoom.class);
        intent.putExtra("some_object", rooms.get(position).getChatName());
        startActivity(intent);
    }
}
