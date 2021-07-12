
package com.leaf.hiddenleadapp.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.UserModels.Users;
import com.leaf.hiddenleadapp.databinding.ActivityMainBinding;
import com.leaf.hiddenleadapp.databinding.ActivitySettingBinding;

import java.util.Objects;
import java.util.Set;

public class SettingActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    ActivitySettingBinding binding;
    Uri selectedImage;
    Users user;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SettingActivity.this);
        progressDialog.setTitle("Loading ...");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();


        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                user = snapshot.getValue(Users.class);
                binding.UserEmail.setText(auth.getCurrentUser().getEmail());
                binding.UserName.setText(user.getUserName());
                binding.UserNameChange.setText(user.getUserName());
                binding.Userbio.setText(user.getUserBio());


                Glide.with(SettingActivity.this).load(user.getProfilePic())
                        .placeholder(R.drawable.avatar)
                        .into(binding.ProfilePIC);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.ProfilePIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this,ProfileChangeActivity.class);
                i.putExtra("profile",user.getProfilePic());
                startActivity(i);
//                finish();
            }
        });

        binding.UserNameSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this,UserUpdationActivity.class);
                i.putExtra("OnClick","UserName");
                i.putExtra("userName",binding.UserNameChange.getText().toString());
                startActivity(i);
            }
        });

        binding.EmailUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,UserUpdationActivity.class);
                intent.putExtra("OnClick","UserEmail");
                intent.putExtra("userEmail",binding.UserEmail.getText().toString());
                startActivity(intent);
            }
        });

        binding.UserBioSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this,UserUpdationActivity.class);
                i.putExtra("OnClick","UserBio");
                i.putExtra("userBio",binding.Userbio.getText().toString());
                startActivity(i);
            }
        });

        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this,PrivacyPolicyActivity.class);
                startActivity(i);
            }
        });


        binding.Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(SettingActivity.this, SignInActivity.class);
                Toast.makeText(SettingActivity.this,"Logout Successful",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finishAffinity();

            }
        });


        binding.About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this,AbtUsActivity.class);
                i.putExtra("OnClick","about");
                startActivity(i);
            }
        });

        binding.ContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this,PrivacyAbtContActivity.class);
                i.putExtra("OnClick","contact");
                startActivity(i);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(data.getData() != null){
                binding.ProfilePIC.setImageURI(data.getData());
                selectedImage = data.getData();
            }

        }
    }
}