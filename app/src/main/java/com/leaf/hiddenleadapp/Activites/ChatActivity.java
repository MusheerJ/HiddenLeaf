package com.leaf.hiddenleadapp.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leaf.hiddenleadapp.Adapters.MessagesAdapter;
import com.leaf.hiddenleadapp.R;

import com.leaf.hiddenleadapp.UserModels.Message;
import com.leaf.hiddenleadapp.databinding.ActivityChatBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    MessagesAdapter adapter;
    ArrayList <Message> messages;
    String senderRoom, receiverRoom;
    FirebaseDatabase database;
    FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();



        String name = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("Uid");
        String userProfile = getIntent().getStringExtra("profile");
        String senderUid = FirebaseAuth.getInstance().getUid();

//        String userProfile = getIntent().getStringExtra("profile");

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(ChatActivity.this,messages,senderRoom,receiverRoom);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        binding.recyclerView.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();

        database.getReference().child("presence").child(receiverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String status = snapshot.getValue(String.class);
                    if (status.equals("offline")){
                        binding.status.setVisibility(View.GONE);
                    }
                    else{
                        binding.status.setText(status);
                        binding.status.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // reading data from the data base
        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            Message message = snapshot1.getValue(Message.class);
//                            message.getMessageId(snapshot1.getKey());
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.scrollToPosition(adapter.getItemCount()- 1);
            }
        });


        binding.messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.attachment.setVisibility(View.VISIBLE);
                    binding.camera.setVisibility(View.VISIBLE);
//                    binding.send.setVisibility(View.VISIBLE);
                    binding.sendText.setVisibility(View.GONE);
                    return;
                }
                binding.attachment.setVisibility(View.GONE);
                binding.camera.setVisibility(View.GONE);
//                binding.send.setVisibility(View.GONE);
                binding.sendText.setVisibility(View.VISIBLE);

            }
        });
        binding.emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });
        // saving data in the data base
        binding.sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String messageTxt = binding.messageBox.getText().toString();

                if(messageTxt.equals("")){
                    return;
                }


                Date date = new Date();
                String strDateFormat = "hh:mm a";
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                String formattedDate= dateFormat.format(date);

                Message message = new Message(messageTxt,senderUid,formattedDate);
                binding.messageBox.setText("");
                message.setMessageId(database.getReference().push().getKey());

                HashMap<String,Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg",message.getMessage());
                lastMsgObj.put("lastMsgTime",message.getTimestamp());
                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(message.getMessageId())
                        .setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                binding.recyclerView.scrollToPosition(adapter.getItemCount()-1);
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .child(message.getMessageId())
                                        .setValue(message)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        });
                            }
                        });


            }
        });

        //
//        String userProfile

        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this,ViewProfileActivity.class);
                i.putExtra("userID",getIntent().getStringExtra("Uid"));
                startActivity(i);
            }
        });

        binding.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this,ViewProfileActivity.class);
                i.putExtra("userID",getIntent().getStringExtra("Uid"));
                startActivity(i);
            }
        });


        Glide.with(this).load(userProfile)
                .placeholder(R.drawable.avatar)
                .into(binding.profile);
        getSupportActionBar().hide();
        binding.name.setText(name);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.chatToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.clearChat:
                        database.getReference().child("chats").child(senderRoom).removeValue();
                        Toast.makeText(ChatActivity.this,"chats cleared",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.viewProfile:
                        Intent i = new Intent(ChatActivity.this,ViewProfileActivity.class);
                        i.putExtra("userID",getIntent().getStringExtra("Uid"));
                        startActivity(i);
                        break;
                    default:
                        break;

                }
                return true;
            }
        });
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference().child("presence").child(uid).setValue("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String uid = FirebaseAuth.getInstance().getUid();
        Date date = new Date();
        String strDateFormat = "hh:mm a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
//        String lastSceen = dateFormat.format(date);
        database.getReference().child("presence").child(auth.getUid()).setValue("offline");
//

    }

    protected void onResume(){
        super.onResume();
        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference().child("presence").child(uid).setValue("active");
    }

}