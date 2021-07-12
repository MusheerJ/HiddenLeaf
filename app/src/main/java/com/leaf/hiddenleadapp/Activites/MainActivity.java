package com.leaf.hiddenleadapp.Activites;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leaf.hiddenleadapp.Adapters.GroupsAdapter;
import com.leaf.hiddenleadapp.Adapters.UsersAdapter;

import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.UserModels.Groups;
import com.leaf.hiddenleadapp.UserModels.Users;
import com.leaf.hiddenleadapp.databinding.ActivityMainBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    ArrayList <Users> users;
    ProgressDialog progressDialog;
    UsersAdapter usersAdapter;
    FirebaseDatabase database;

    ArrayList <Groups> groups;
    GroupsAdapter groupsAdapter;
    final String UID = FirebaseAuth.getInstance().getUid();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading Chats ..... ");
        progressDialog.setMessage("Please wait for a while ....");
        progressDialog.show();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(this,users);
        binding.recyclerView.setAdapter(usersAdapter);




        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 :snapshot.getChildren()){
                    Users user = snapshot1.getValue(Users.class);
                    if (user.getUserId().equals(auth.getUid())){
                        continue;
                    }
                    users.add(user);
                }
                progressDialog.dismiss();
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        groups = new ArrayList<>();
        groupsAdapter = new GroupsAdapter(this,groups);
        binding.groupRecylerView.setAdapter(groupsAdapter);

        database.getReference().child("groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Toast.makeText(MainActivity.this,"Loaded",Toast.LENGTH_SHORT).show();
                progressDialog.show();
                groups.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Groups group = snapshot1.getValue(Groups.class);
                    groups.add(group);
                }
                progressDialog.dismiss();
                groupsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.chats:
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        binding.groupRecylerView.setVisibility(View.GONE);
                        break;
                    case R.id.Leaflets:
                        Toast.makeText(MainActivity.this,
                                "Will be availble soon ..",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.group:
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.groupRecylerView.setVisibility(View.VISIBLE);

                        break;
                }
                return true;
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
//        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(UID).setValue("active");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        String currentId = FirebaseAuth.getInstance().getUid();
        Date date = new Date();
        String strDateFormat = "hh:mm a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String lastSceen = "last seen "+ dateFormat.format(date);
        database.getReference().child("presence").child(UID).setValue("offline");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //search
    private void filter(String keyword){
        binding.noResultFound.setVisibility(View.GONE);

        ArrayList <Users> filteredList = new ArrayList<>();
        if (keyword.isEmpty()){
            filteredList.addAll(users);
        }
        else {
            for (Users user : users) {
                if (user.getUserName().toLowerCase().contains(keyword.toLowerCase()) || user.getMail().toLowerCase().contains(keyword.toLowerCase()) ) {
                    filteredList.add(user);
                }
            }
            if (filteredList.isEmpty()){
                binding.noResultFound.setVisibility(View.VISIBLE);
            }
            else{
                binding.noResultFound.setVisibility(View.GONE);
            }

        }
        usersAdapter.filterList(filteredList);

    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {


            case R.id.Settings :
                Intent intent1 = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent1);
                break;
            case R.id.LogOut:
//                auth.signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                Toast.makeText(MainActivity.this,"Logout Successful",Toast.LENGTH_SHORT).show();
                auth.signOut();
                startActivity(intent);
                finishAffinity();
                break;
            case R.id.groups:
                Intent i = new Intent(MainActivity.this,CreateGroupActivity.class);
                startActivity(i);
                break;

        }
        return true;
    }

}