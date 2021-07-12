package com.leaf.hiddenleadapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leaf.hiddenleadapp.Activites.GroupChatActivity;
import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.UserModels.Groups;

import com.leaf.hiddenleadapp.databinding.GroupChatConvBinding;


import java.util.ArrayList;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder>{
    Context context;
    ArrayList <Groups> groups;
    public GroupsAdapter(Context context , ArrayList<Groups> groups){
        this.context = context;
        this.groups = groups;
    }


    @NonNull
    @Override
    public GroupsAdapter.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.group_chat_conv,parent,false);
        return new GroupsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsViewHolder holder, int position) {
        Groups group = groups.get(position);
        holder.binding.groupname.setText(group.getGroupName());
//        holder.binding.username.setText(user.getUserName());
        Glide.with(context).load(group.getGroupProfile())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.groupprofile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("name",group.getGroupName());
//                intent.putExtra("uid",user.getUserId());
                intent.putExtra("Uid",group.getGroupId());
                intent.putExtra("descrip",group.getGroupDescription());
                intent.putExtra("profile",group.getGroupProfile());
                context.startActivity(intent);
            }
        });

//        holder.binding.profile.setImageResource();

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class GroupsViewHolder extends RecyclerView.ViewHolder{

        GroupChatConvBinding binding;
        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = GroupChatConvBinding.bind(itemView);
        }
    }
}
