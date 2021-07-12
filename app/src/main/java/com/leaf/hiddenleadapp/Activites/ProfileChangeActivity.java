package com.leaf.hiddenleadapp.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.databinding.ActivityProfileChangeBinding;

public class ProfileChangeActivity extends AppCompatActivity {
    ActivityProfileChangeBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Uri selectedImage;
    ProgressDialog dialog;
    String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileChangeBinding.inflate(getLayoutInflater());
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        setContentView(binding.getRoot());
        dialog = new ProgressDialog(ProfileChangeActivity.this);
        dialog.setTitle("Updating your profile");
        dialog.setMessage("Please wait......");

        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String profilePic = getIntent().getStringExtra("profile");

        Glide.with(ProfileChangeActivity.this).load(profilePic)
                .placeholder(R.drawable.avatar)
                .into(binding.ProfileSetUp);


        binding.ProfileSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);

            }
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if (selectedImage != null){
                    StorageReference reference = storage.getReference()
                            .child("Profiles")
                            .child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUri = uri.toString();
                                        database.getReference()
                                                .child("Users")
                                                .child(auth.getUid())
                                                .child("ProfilePic")
                                                .setValue(imageUri)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }

                        }
                    });


                }
                else
                {
                    if (profilePic.isEmpty())
                    {
                        imageUri = "No profile";
                    }
                    else {
                        imageUri = profilePic;
                    }
                    database.getReference()
                            .child("Users")
                            .child(auth.getUid())
                            .child("ProfilePic")
                            .setValue(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    Intent i = new Intent(ProfileChangeActivity.this,SettingActivity.class);
                                    dialog.dismiss();
//                                    startActivity(i);
                                    finish();
                                }
                            });

                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            binding.ProfileSetUp.setImageURI(data.getData());
            selectedImage = data.getData();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}