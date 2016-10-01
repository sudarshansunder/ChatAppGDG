package com.example.chat;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private ImageButton sendButton;
    private EditText sendMessage;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private static ArrayList<Message> list = new ArrayList<>();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().getRoot();
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        Log.d("Firebase Token", FirebaseInstanceId.getInstance().getToken());
        sendButton = (ImageButton) findViewById(R.id.sendImageButton);
        sendMessage = (EditText) findViewById(R.id.messageText);
        recyclerView = (RecyclerView) findViewById(R.id.messageScrollView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        adapter = new MessageAdapter(this, list);
        recyclerView.setAdapter(adapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sendMessage.getText().toString().isEmpty()) {
                    addToDatabase();
                    sendNotification();
                    sendMessage.setText("");
                } else {
                    Snackbar.make(view, "Message cannot be empty", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    String message = (String) ((DataSnapshot) i.next()).getValue();
                    String sender = (String) ((DataSnapshot) i.next()).getValue();
                    String timestamp = (String) ((DataSnapshot) i.next()).getValue();
                    boolean self;
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null && user.getEmail().equals(sender)) {
                        self = true;
                    } else
                        self = false;
                    Message m = new Message(message, sender, timestamp, self);
                    list.add(m);
                }
                adapter = new MessageAdapter(MainActivity.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addToDatabase() {
        String message = sendMessage.getText().toString();
        String sender = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            sender = user.getEmail();
        } else {
            Toast.makeText(getApplicationContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }
        HashMap<String, Object> map = new HashMap<>();
        String temp = rootRef.push().getKey();
        rootRef.updateChildren(map);
        DatabaseReference messageRef = rootRef.child(temp);
        map = null;
        map = new HashMap<>();
        map.put("sender", sender);
        map.put("message", message);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date()); // Find todays date
        map.put("timestamp", currentDateTime);
        messageRef.updateChildren(map);
    }

    private void sendNotification() {

        final String URL = "http://localhost:3000/fcm/send";
        JSONObject payload = new JSONObject();
        try {
            payload.put("message", sendMessage.getText().toString());
            payload.put("token", FirebaseInstanceId.getInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue.add(new JsonObjectRequest(Request.Method.POST, URL, payload, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Send successful", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Send message error", error.toString());
            }
        }));
    }
}
