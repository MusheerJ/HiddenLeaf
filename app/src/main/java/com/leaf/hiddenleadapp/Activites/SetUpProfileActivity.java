package com.leaf.hiddenleadapp.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leaf.hiddenleadapp.UserModels.Users;
import com.leaf.hiddenleadapp.databinding.ActivitySetUpProfileBinding;

public class SetUpProfileActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    ActivitySetUpProfileBinding binding;
    Uri selectedImage;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetUpProfileBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        dialog = new ProgressDialog(SetUpProfileActivity.this);
        dialog.setTitle("Setting your profile");
        dialog.setMessage("Please wait......");

        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        Intent intent1 = getIntent();
        String UserID = intent1.getStringExtra("ID");
        String prev = intent1.getStringExtra("prev");





        binding.ProfileSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);

            }
        });



        binding.Setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(binding.NameBox.getText().toString().isEmpty()) {
                    binding.NameBox.setError("Please Type a name");
                    return;
                }
                if (binding.NameBox.getText().toString().length() >= 42){
                    binding.NameBox.setError("Bio must be a string with max length 42");
                    return;
                }

                dialog.show();
                if (selectedImage != null ){
                    StorageReference reference = storage.getReference()
                            .child("Profiles")
                            .child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri)
                                    {
                                    //Saving data of User who you Email Auth
                                     {
                                         String imageUri = uri.toString();
                                         String UserBio = binding.NameBox.getText().toString();
//                                         Users users = new Users(imageUri,UserBio);
                                         database.getReference()
                                                 .child("Users")
                                                 .child(UserID)
                                                 .child("ProfilePic")
                                                 .setValue(imageUri)
                                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                     @Override
                                                     public void onSuccess(Void aVoid) {
                                                         database.getReference()
                                                                 .child("Users")
                                                                 .child(UserID)
                                                                 .child("UserBio")
                                                                 .setValue(UserBio)
                                                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                     @Override
                                                                     public void onSuccess(Void aVoid) {

                                                                     }
                                                                 });
                                                         Intent intent = new Intent(SetUpProfileActivity.this,MainActivity.class);
                                                         startActivity(intent);
                                                         dialog.dismiss();
                                                         finish();

                                                     }
                                                 });

                                    }

                                    }
                                });
                            }
                        }
                    });
                }

                else
                {

                    //Saving data of User who you Email Auth
                     { String UserBio = binding.NameBox.getText().toString();
                        String imageUri = "No profile";
                        Users users = new Users(imageUri,UserBio);
                        database.getReference()
                                .child("Users")
                                .child(UserID)
                                .child("ProfilePic")
                                .setValue(users.getProfilePic())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        database.getReference()
                                                .child("Users")
                                                .child(UserID)
                                                .child("UserBio")
                                                .setValue(users.getUserBio())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });
                                        Intent intent = new Intent(SetUpProfileActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        finish();

                                    }
                                });

                    }


                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(data.getData() != null){
                binding.ProfileSetUp.setImageURI(data.getData());
                selectedImage = data.getData();
            }


        }
    }
}