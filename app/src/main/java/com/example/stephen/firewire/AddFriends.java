package com.example.stephen.firewire;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddFriends extends AppCompatActivity {

    User mainUser;
    Dialog friendUpAction;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        final EditText searchUser = (EditText)findViewById(R.id.editText4);
        ImageView search = (ImageView)findViewById(R.id.imageView);
        final LinearLayout ll = (LinearLayout)findViewById(R.id.llFriends);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        friendUpAction = new Dialog(this);

        Intent intent = getIntent();
        mainUser= (User)intent.getSerializableExtra("User");


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference searchReference = FirebaseDatabase.getInstance().getReference("users/"+searchUser.getText().toString().trim().toLowerCase());

                try {
                    searchReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final User posFriend = dataSnapshot.getValue(User.class);



                            Button myButton = new Button(AddFriends.this);
                            myButton.setText(posFriend.username);

                            myButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    friendPopUp(view, posFriend.username);

                                }
                            });


                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            ll.addView(myButton, lp);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                catch (Exception e){
                    Toast.makeText(AddFriends.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void friendPopUp(View v, final String friend){
        friendUpAction.setContentView(R.layout.friend_layout);
        TextView close = (TextView) friendUpAction.findViewById(R.id.closex);
        TextView name = (TextView) friendUpAction.findViewById(R.id.textView11);
        //TextView username = (TextView) friendUpAction.findViewById(R.id.textView12);


        name.setText("@"+friend);

        ImageView add = (ImageView)friendUpAction.findViewById(R.id.imageView4);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DatabaseReference searchReference = FirebaseDatabase.getInstance().getReference(friend.getUsername());
                DatabaseReference standardReference = FirebaseDatabase.getInstance().getReference();

                    if (!mainUser.getFriends().contains(friend)) {

                        mainUser.addFriend(friend);
                        //friend.addFriend(mainUser);

                        //if (standardReference.childExists)
                        //standardReference.child("idkifiwantthistofailornot").setValue("string");
                        //standardReference.child("users").child(friend.getUsername()).push().setValue(friend);
                        standardReference.child("users").child(mainUser.username.toLowerCase()).setValue(mainUser);
                        Toast.makeText(AddFriends.this, friend + " has been added to your friends", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(AddFriends.this, friend + " is already your friend!", Toast.LENGTH_LONG).show();
                    }


            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendUpAction.dismiss();
            }
        });

        friendUpAction.show();
    }
}
