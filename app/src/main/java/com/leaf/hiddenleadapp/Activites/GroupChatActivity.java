package com.leaf.hiddenleadapp.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.leaf.hiddenleadapp.Adapters.MessagesAdapter;
import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.UserModels.Message;
import com.leaf.hiddenleadapp.databinding.ActivityGroupChatBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;
    MessagesAdapter adapter;
    ArrayList<Message> messages;
    FirebaseDatabase database;
    String senderUid;
    String groupProfile , groupName , groupDescription , groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        senderUid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        messages = new ArrayList<>();

        getSupportActionBar().hide();

        groupName = getIntent().getStringExtra("name");
        groupDescription = getIntent().getStringExtra("descrip");
        groupProfile = getIntent().getStringExtra("profile");
        groupId = getIntent().getStringExtra("Uid");


        binding.name.setText(groupName);
        Glide.with(this).load(groupProfile)
                .placeholder(R.drawable.avatar)
                .into(binding.groupprofile);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.send.setOnClickListener(new View.OnClickListener() {
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

                database.getReference().child("group")
                        .push()
                        .setValue(message);
            }
        });

        binding.chatToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.viewGroupProfile:
//                        database.getReference().child("chats").child(senderRoom).removeValue();
                        Toast.makeText(GroupChatActivity.this,"Group chats cleared",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.clearGroupChat:
                        Toast.makeText(GroupChatActivity.this,"Group chats cleared",Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        break;

                }

                return true;
            }
        });


    }
}