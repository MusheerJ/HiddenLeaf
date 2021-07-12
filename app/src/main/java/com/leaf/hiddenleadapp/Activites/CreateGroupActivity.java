package com.leaf.hiddenleadapp.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.UserModels.Groups;
import com.leaf.hiddenleadapp.databinding.ActivityCreateGroupBinding;
import com.leaf.hiddenleadapp.databinding.ActivityGroupChatBinding;

public class CreateGroupActivity extends AppCompatActivity {
    ActivityCreateGroupBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    Uri selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);


        Groups group = new Groups();
        setContentView(binding.getRoot());



        binding.set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedImage != null){
                    StorageReference reference = storage.getReference()
                            .child("GroupsProfiles")
                            .child(auth.getUid());

                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        group.setGroupName(binding.groupName.getText().toString());
                                        group.setGroupDescription(binding.groupDescription.getText().toString());
                                        group.setGroupProfile(uri.toString());
                                        group.setGroupId(database.getReference().push().getKey());
                                        database.getReference().child("groups")
                                                .child(group.getGroupId())
                                                .setValue(group);
                                        binding.groupName.setText("");
                                        binding.groupDescription.setText("");

                                    }
                                });
                            }
                        }
                    });



                }
                else
                    {
                        group.setGroupName(binding.groupName.getText().toString());
                        group.setGroupDescription(binding.groupDescription.getText().toString());
                        group.setGroupProfile("No profile");
                        group.setGroupId(database.getReference().push().getKey());
                        database.getReference().child("groups")
                                .child(group.getGroupId())
                                .setValue(group);
                        binding.groupName.setText("");
                        binding.groupDescription.setText("");

                    }

            }

        });
        binding.groupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(data.getData() != null){
                binding.groupProfile.setImageURI(data.getData());
                selectedImage = data.getData();
            }

        }
    }


}