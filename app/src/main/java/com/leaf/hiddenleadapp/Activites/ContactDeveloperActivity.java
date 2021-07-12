package com.leaf.hiddenleadapp.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.databinding.ActivityContactDeveloperBinding;

public class ContactDeveloperActivity extends AppCompatActivity {

    ActivityContactDeveloperBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityContactDeveloperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();



        binding.DeveloperEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"musheerjamadar1024@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"");
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
                else
                    Toast.makeText(ContactDeveloperActivity.this,"Gmail App is not installed", Toast.LENGTH_SHORT).show();
            }
        });

        binding.DeveloperInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/musheerjr/";
                Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(url) );
                ContactDeveloperActivity.this.startActivity(browse);
            }
        });

        binding.DeveloperPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the intent and set the data for the
                // intent as the phone number.
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7620196445"));
                startActivity(intent);
            }
        });
    }
}