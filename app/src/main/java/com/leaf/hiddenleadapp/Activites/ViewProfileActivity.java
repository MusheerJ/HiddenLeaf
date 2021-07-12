package com.leaf.hiddenleadapp.Activites;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.UserModels.Users;
import com.leaf.hiddenleadapp.databinding.ActivityViewProfileBinding;

public class ViewProfileActivity extends AppCompatActivity {

    ActivityViewProfileBinding binding;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            binding = ActivityViewProfileBinding.inflate(getLayoutInflater());
            database = FirebaseDatabase.getInstance();
            setContentView(binding.getRoot());
            String Userid = getIntent().getStringExtra("userID");
            getSupportActionBar().setTitle("User profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            database.getReference("Users").child(Userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users user = snapshot.getValue(Users.class);
                    binding.viewUserName.setText(user.getUserName());
                    binding.viewUserBio.setText(user.getUserBio());
                    binding.viewUserMail.setText(user.getMail());

                    Glide.with(ViewProfileActivity.this).load(user.getProfilePic())
                            .placeholder(R.drawable.avatar)
                            .into(binding.profileView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }






    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}