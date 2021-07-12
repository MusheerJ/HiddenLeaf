package com.leaf.hiddenleadapp.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.databinding.ActivityContactDesignerBinding;
import com.leaf.hiddenleadapp.databinding.ActivityContactDeveloperBinding;

public class ContactDesignerActivity extends AppCompatActivity {
    ActivityContactDesignerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactDesignerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        binding.DesignerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"prathameshsawantdesign@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"");
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
                else
                    Toast.makeText(ContactDesignerActivity.this,"Gmail App is not installed", Toast.LENGTH_SHORT).show();
            }
        });

        binding.DesignerInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/prathamesh_sawant2925/";
                Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(url) );
                ContactDesignerActivity.this.startActivity(browse);
            }
        });

        binding.DesignerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:8788169910"));
                startActivity(intent);
            }
        });
    }
}