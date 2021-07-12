package com.leaf.hiddenleadapp.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.databinding.ActivityContactClubBinding;

public class ContactClubActivity extends AppCompatActivity {

    ActivityContactClubBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactClubBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();


        binding.ClubEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"oysterkode@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"");
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
                else
                    Toast.makeText(ContactClubActivity.this,"Gmail App is not installed", Toast.LENGTH_SHORT).show();
            }

        });

        binding.ClubInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/oyster_kode/";
                Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(url) );
                ContactClubActivity.this.startActivity(browse);
            }
        });


        binding.ClubWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}