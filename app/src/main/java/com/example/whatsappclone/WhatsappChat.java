package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class WhatsappChat extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarChat;
    private ListView listViewChat;
    private EditText edtMessage;
    private Button btnSendMessage;
    private String chatUser;
    private ArrayList<String> allMessage;
    private ArrayAdapter<String> mStringArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_chat);
        toolbarChat = findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbarChat);
        chatUser = getIntent().getStringExtra("user");
        listViewChat = findViewById(R.id.listChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(this);
        allMessage = new ArrayList<>();
        mStringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allMessage);
        listViewChat.setAdapter(mStringArrayAdapter);
        ParseQuery<ParseObject> firstUserQuery = ParseQuery.getQuery("Chat");
        ParseQuery<ParseObject> secondUserQuery = ParseQuery.getQuery("Chat");
        firstUserQuery.whereEqualTo("sender",ParseUser.getCurrentUser().getUsername());
        firstUserQuery.whereEqualTo("recipient",chatUser);

        secondUserQuery.whereEqualTo("sender",chatUser);
        secondUserQuery.whereEqualTo("recipient",ParseUser.getCurrentUser().getUsername());
        ArrayList<ParseQuery<ParseObject>> allQuery = new ArrayList<>();
        allQuery.add(firstUserQuery);
        allQuery.add(secondUserQuery);
        ParseQuery<ParseObject> query = ParseQuery.or(allQuery);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size()>0 && e == null){
                    for (ParseObject object:objects){
                        String message = object.get("message")+"";
                        if (object.get("sender").equals(ParseUser.getCurrentUser().getUsername())){
                            message = ParseUser.getCurrentUser().getUsername() + " : " + message;
                        }
                        if (object.get("recipient").equals(ParseUser.getCurrentUser().getUsername())){
                            message = chatUser + " : "+ message;
                        }
                        allMessage.add(message);
                    }
                    mStringArrayAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(WhatsappChat.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        sendMessageToParse();
    }

    private void sendMessageToParse() {
        final ParseObject user = new ParseObject("Chat");
        user.put("sender", ParseUser.getCurrentUser().getUsername());
        user.put("recipient", chatUser);
        user.put("message", edtMessage.getText().toString());
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    allMessage.add(ParseUser.getCurrentUser().getUsername() + " : " + edtMessage.getText().toString());
                    mStringArrayAdapter.notifyDataSetChanged();
                    edtMessage.getText().clear();
                } else {
                    Toast.makeText(WhatsappChat.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
