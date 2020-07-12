package com.example.chatapp;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayAllMessagesInTheRoom extends AppCompatActivity {
    private FloatingActionButton sendBtn;
    private FirebaseListAdapter<Message> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String chatName = getIntent().getStringExtra("some_object");
        TextView title = findViewById(R.id.title_forChat);
        title.setText(chatName);

        ListView listOFMessages = findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<Message>(this,
                Message.class,
                R.layout.list_item,
                FirebaseDatabase.getInstance().getReference().child("rooms").child(chatName)) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView mess_user, mess_time;
                BubbleTextView mess_text;
                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                mess_text = v.findViewById(R.id.message_text);

                mess_user.setText(model.getUserName());
                mess_text.setText(model.getTextMessage());
                mess_time.setText(DateFormat.format("HH:mm (dd MMM yyyy)", model.getMessageTime()));
            }
        };

        listOFMessages.setAdapter(adapter);

        sendBtn = findViewById(R.id.btnSend);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField = findViewById(R.id.messageField);
                if (textField.getText().toString().equals(""))
                    return;

                FirebaseDatabase.getInstance().getReference().child("rooms").child(chatName).push().setValue(
                        new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                textField.getText().toString()
                        )
                );
                textField.setText("");
            }
        });
    }
}
