package com.leaf.hiddenleadapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.databinding.ActivityUserUpdationBinding;

public class UserUpdationActivity extends AppCompatActivity {

    ActivityUserUpdationBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        binding = ActivityUserUpdationBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();

        setContentView(binding.getRoot());

        String onClick = getIntent().getStringExtra("OnClick");



        switch (onClick)
        {
            case "UserName":
                String userName = getIntent().getStringExtra("userName");
                binding.UserDetailsUpdate.setText(userName);
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                binding.mytoolbar.setTitle("Username");
                break;
            case "UserEmail":
                String userEmail = getIntent().getStringExtra("userEmail");
                binding.UserDetailsUpdate.setText(userEmail);
                binding.info.setText("Email can't be updated. You can just see the email and changes wont be applied!!");
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                binding.mytoolbar.setTitle("Email");
                break;
            case "UserBio":
                String userBio = getIntent().getStringExtra("userBio");
                binding.UserDetailsUpdate.setText(userBio);
                binding.UserDetailsUpdate.setHint("Your bio");
                binding.info.setText("Edit the bio and click on the check button to update your bio");
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                binding.mytoolbar.setTitle("Bio");
                break;
            default:
                break;
        }

        binding.mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.mytoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.Done){
                    String onClick = getIntent().getStringExtra("OnClick");
                    onStart(onClick);
                }
                return true;
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().hide();
//        binding.toolbar.setTitle();
    }



    protected void onStart(String onclick) {
        super.onStart();
        if ("UserName".equals(onclick)) {
            if (binding.UserDetailsUpdate.getText().toString().isEmpty()) {
                binding.UserDetailsUpdate.setError("Username cant be empty!!");
                return;
            }

            if (binding.UserDetailsUpdate.getText().toString().length() >= 18) {
                binding.UserDetailsUpdate.setError("username must be a string with max length 18!!");
                return;
            }
            database.getReference("Users").child(auth.getUid()).child("userName").setValue(binding.UserDetailsUpdate.getText().toString());
            Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();
        }
        else if ("UserEmail".equals(onclick))
        {
            Toast.makeText(this, "Email cant be updated !!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if ("UserBio".equals(onclick)){
            if (binding.UserDetailsUpdate.getText().toString().isEmpty()) {
                binding.UserDetailsUpdate.setError("Bio cant be empty!!");
                return;
            }
            if (binding.UserDetailsUpdate.getText().toString().length() >= 42) {
                binding.UserDetailsUpdate.setError("Bio must be a string with max length 42");
                return;
            }
            database.getReference("Users").child(auth.getUid()).child("UserBio").setValue(binding.UserDetailsUpdate.getText().toString());
            Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

}