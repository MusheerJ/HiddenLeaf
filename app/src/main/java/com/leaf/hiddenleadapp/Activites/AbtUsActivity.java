package com.leaf.hiddenleadapp.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.databinding.ActivityAbtUsMainBinding;

public class AbtUsActivity extends AppCompatActivity {
    ActivityAbtUsMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAbtUsMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
    }
}