package com.leaf.hiddenleadapp.Adapters;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.UserModels.Message;
import com.leaf.hiddenleadapp.databinding.DeleteDialogBinding;
import com.leaf.hiddenleadapp.databinding.ItemReceiveBinding;
import com.leaf.hiddenleadapp.databinding.ItemSent1Binding;
import com.leaf.hiddenleadapp.databinding.MessageOptionsBinding;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList <Message>  messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    String senderRoom;
    String receiverRoom;

    public MessagesAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent1,parent,false);
            return new SentViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){
            return ITEM_SENT;
        }
        else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){

        Message message = messages.get(position);


        if(holder.getClass() == SentViewHolder.class){

            View view = LayoutInflater.from(context).inflate(R.layout.message_options,null);
            MessageOptionsBinding msgbinding = MessageOptionsBinding.bind(view);
            AlertDialog msg = new AlertDialog.Builder(context)
                    .setTitle("Message options")
                    .setView(msgbinding.getRoot())
                    .create();
            msgbinding.openLink.setVisibility(View.GONE);
            SentViewHolder viewHolder = (SentViewHolder)holder;
            viewHolder.binding.message.setText(message.getMessage());
            viewHolder.binding.msgTimeStamp.setText(message.getTimestamp());

            //check if Link
            if(message.getMessage().toLowerCase().contains("http://") || message.getMessage().toLowerCase().contains("https://")){
                viewHolder.binding.message.setTextColor(Color.parseColor("#34b7f1"));
                msgbinding.openLink.setVisibility(View.VISIBLE);
                msgbinding.copymessage.setText("Copy URL link");
            }
            //ON_CLICK
            viewHolder.binding.message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    msg.show();


                    //delete Message
                    msgbinding.deletemessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            msgDelete(message);
                            msg.dismiss();
                        }
                    });

                    //open Link
                    msgbinding.openLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openUrlLink(message.getMessage());
                            msg.dismiss();
                        }
                    });

                    //copy Message
                    msgbinding.copymessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setText(viewHolder.binding.message.getText());
                            Toast.makeText(context,"copied",Toast.LENGTH_SHORT).show();
                            msg.dismiss();
                        }
                    });

                }
            });

            // LONG CLICK
            viewHolder.binding.message.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog,null);
                    DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message ?")
                            .setView(binding.getRoot())
                            .create();

                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageId()).setValue(null)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialog.dismiss();
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("chats")
                                                    .child(receiverRoom)
                                                    .child("messages")
                                                    .child(message.getMessageId())
                                                    .setValue(null)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    viewHolder.binding.message.setText("This message is deleted !");
                                                }
                                            });

                                        }
                                    });
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageId()).setValue(null)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(context,"Message deleted",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });

                    binding.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return true;
                }
            });
        }
        else if (holder.getClass() == ReceiverViewHolder.class){

            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.binding.message.setText(message.getMessage());
            viewHolder.binding.msgTimeStampReciver.setText(message.getTimestamp());
            View view = LayoutInflater.from(context).inflate(R.layout.message_options,null);
            MessageOptionsBinding msgbinding = MessageOptionsBinding.bind(view);
            AlertDialog msg = new AlertDialog.Builder(context)
                    .setTitle("Message options")
                    .setView(msgbinding.getRoot())
                    .create();

            msgbinding.openLink.setVisibility(View.GONE);


            //check if Link
            if(message.getMessage().toLowerCase().contains("http://") || message.getMessage().toLowerCase().contains("https://")){
                viewHolder.binding.message.setTextColor(Color.parseColor("#34b7f1"));
                msgbinding.openLink.setVisibility(View.VISIBLE);
                msgbinding.copymessage.setText("Copy URL link");
            }
            //ON_CLICK
            viewHolder.binding.message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    msg.show();
                    //delete Message
                    msgbinding.deletemessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageId()).setValue(null)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(context,"Message deleted",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            msg.dismiss();
                        }
                    });

                    //open Link
                    msgbinding.openLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openUrlLink(message.getMessage());
                            msg.dismiss();
                        }
                    });

                    //copy Message
                    msgbinding.copymessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setText(viewHolder.binding.message.getText());
                            Toast.makeText(context,"copied",Toast.LENGTH_SHORT).show();
                            msg.dismiss();
                        }
                    });

                }
            });


        }



    }


    //deleteMessage
    public void msgDelete(Message message){

        View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog,null);
        DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Delete Message ?")
                .setView(binding.getRoot())
                .create();

        binding.everyone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(message.getMessageId()).setValue(null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .child(message.getMessageId())
                                        .setValue(null)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        });

                            }
                        });
            }
        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(message.getMessageId()).setValue(null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(context,"Message deleted",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder{
        ItemSent1Binding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSent1Binding.bind(itemView);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        ItemReceiveBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }


    public void openUrlLink(String url){
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(url) );
        context.startActivity(browse);

    }



}
