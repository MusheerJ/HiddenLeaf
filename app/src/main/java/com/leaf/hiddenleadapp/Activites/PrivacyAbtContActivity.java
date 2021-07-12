package com.leaf.hiddenleadapp.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
//import   ActivityPrivacyAbtContBinding;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.databinding.ActivityPrivacyAbtContBinding;
import com.leaf.hiddenleadapp.databinding.ContactUsDialogBinding;

public class PrivacyAbtContActivity extends AppCompatActivity {

    FirebaseDatabase database;
    ActivityPrivacyAbtContBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityPrivacyAbtContBinding.inflate(getLayoutInflater());
        database = FirebaseDatabase.getInstance();
        setContentView(binding.getRoot());
        String email = "oysterkode@gmail.com";
        String privacy = "This is privacy policy";
        String about = "MusheerAhmed Jamadar";

        String onClick = getIntent().getStringExtra("OnClick");

        switch (onClick)
        {
            case "privacy":
                getSupportActionBar().setTitle("Privacy Policy");
                binding.privatemail.setVisibility(View.GONE);
                binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                binding.PPAC.setText(privacy);
                break;
            case "about":
                getSupportActionBar().setTitle("About us");
                binding.privatemail.setVisibility(View.GONE);
                binding.PPAC.setText(about);
                break;
            case "contact":
                getSupportActionBar().setTitle("Contact us");
                binding.privatemail.setVisibility(View.GONE);
                binding.PPAC.setVisibility(View.GONE);
//                binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                binding.PPAC.setTextColor(Color.parseColor("#34b7f1"));
                binding.PPAC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoUrl();
                    }
                });
                binding.PPAC.setText(email);
            default:
                break;
        }

//        binding.contactClubBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View view = LayoutInflater.from(PrivacyAbtContActivity.this).inflate(R.layout.contact_us_dialog,null);
//                ContactUsDialogBinding contactBinding = ContactUsDialogBinding.bind(view);
//                AlertDialog msg = new AlertDialog.Builder(PrivacyAbtContActivity.this)
//                        .setTitle("Club contact")
//                        .setView(contactBinding.getRoot())
//                        .create();
//
//                msg.show();
//            }
//        });
        binding.contactClubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrivacyAbtContActivity.this,ContactClubActivity.class));
            }
        });

        binding.contactDeveloperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrivacyAbtContActivity.this,ContactDeveloperActivity.class));
            }
        });

        binding.contactDesignerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrivacyAbtContActivity.this,ContactDesignerActivity.class));
            }
        });
    }

    public void gotoUrl( ) {
        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"oysterkode@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT,"");
        intent.setPackage("com.google.android.gm");
        if (intent.resolveActivity(getPackageManager())!=null)
            startActivity(intent);
        else
            Toast.makeText(this,"Gmail App is not installed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}