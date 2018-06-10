package com.example.stephen.firewire;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FriendInviteRecyclerAdapter extends RecyclerView.Adapter<FriendInviteRecyclerAdapter.ViewHolder> {

        private ArrayList<String> friends;
        //private ArrayList<String> invites;
        Context context;

        public FriendInviteRecyclerAdapter(Context context, ArrayList<String> friends) {
            this.friends = friends;
            this.context=context;
        }

        @NonNull
        @Override
        public FriendInviteRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_picke_icon, parent, false);
            FriendInviteRecyclerAdapter.ViewHolder holder = new FriendInviteRecyclerAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final FriendInviteRecyclerAdapter.ViewHolder holder, final int position) {


            holder.name.setText(friends.get(position));


            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, friends.get(position), Toast.LENGTH_LONG).show();

                    holder.invited.toggle();
                    if (holder.invited.isChecked()){
                        //invites.add(friends.get(position));
                        MainActivity.invites.add(friends.get(position).toLowerCase());
                    }
                    else{
                        MainActivity.invites.remove(friends.get(position).toLowerCase());
                        //invites.remove(friends.get(position));
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return friends.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            CheckBox invited;
            TextView name;
            ConstraintLayout parentLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.friend_name);


                invited = itemView.findViewById(R.id.radioButton);
                parentLayout = itemView.findViewById(R.id.invites_layout);

            }
        }
    }



