package com.leaf.hiddenleadapp.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.leaf.hiddenleadapp.databinding.ActivityPhoneAuthBinding;

import java.util.Objects;


public class PhoneAuthActivity extends AppCompatActivity {

    ActivityPhoneAuthBinding binding;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);


    Objects.requireNonNull(getSupportActionBar()).hide();
    binding = ActivityPhoneAuthBinding.inflate(getLayoutInflater());
    getSupportActionBar().hide();
    setContentView(binding.getRoot());


    binding.PhoneNumber.requestFocus();

    binding.Continue.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View View) {
            Intent intent = new Intent(PhoneAuthActivity.this, OTPActivity.class);
            intent.putExtra("phoneNumber","+91"+binding.PhoneNumber.getText().toString());
            startActivity(intent);


        }
    });
    }
}