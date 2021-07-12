package com.leaf.hiddenleadapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leaf.hiddenleadapp.Activites.ChatActivity;
import com.leaf.hiddenleadapp.R;

import com.leaf.hiddenleadapp.UserModels.Users;
import com.leaf.hiddenleadapp.databinding.RowConversationBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    Context context;
    ArrayList <Users> users;

    public UsersAdapter(Context context , ArrayList<Users> users){
        this.context = context;
        this.users = users;

    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation,parent,false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        Users user = users.get(position);
        holder.binding.username.setText(user.getUserName());

        String senderRoom =  FirebaseAuth.getInstance().getUid() + user.getUserId();

        FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            String lastMsgTime = snapshot.child("lastMsgTime").getValue(String.class);
                            holder.binding.lastMsg.setText(lastMsg);
                            holder.binding.msgTime.setText(lastMsgTime);
                        }
                        else{
                            holder.binding.lastMsg.setText("Tap to chat");
                            holder.binding.msgTime.setText("");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        Glide.with(context).load(user.getProfilePic())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.profile);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name",user.getUserName());
//                intent.putExtra("uid",user.getUserId());
                intent.putExtra("Uid",user.getUserId());
                intent.putExtra("profile",user.getProfilePic());
//                intent.putExtra("profile",user.getProfilePic());
                context.startActivity(intent);
            }
        });

//        holder.binding.profile.setImageResource();

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class UsersViewHolder extends RecyclerView.ViewHolder{

        RowConversationBinding binding;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowConversationBinding.bind(itemView);
        }
    }

    public void filterList(ArrayList<Users> filteredList){
        users = filteredList;
        notifyDataSetChanged();
    }


}
