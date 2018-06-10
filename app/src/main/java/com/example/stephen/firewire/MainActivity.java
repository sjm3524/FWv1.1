package com.example.stephen.firewire;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    Dialog popUpAction;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference usernameReference;
    User mainUser;
    public static ArrayList<String> invites;

    public String username;


    //String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        invites = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        usernameReference = FirebaseDatabase.getInstance().getReference("ids/"+fbUser.getUid());



        final FirebaseDatabase database = FirebaseDatabase.getInstance();



        //DatabaseReference ref = database.getReference();

// Attach a listener to read the data at our posts reference
        usernameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               MainActivity.this.username = (String)dataSnapshot.getValue();
                //System.out.println(post);

                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users/"+MainActivity.this.username.toLowerCase());

                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mainUser = dataSnapshot.getValue(User.class);


                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_LONG).show();
                                totalList();
                                initFriendRecycler();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });




        popUpAction = new Dialog(this);


        firebaseAuth = FirebaseAuth.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionPopUp(view);
            }
        });
    }

    public void actionPopUp(View v){
        popUpAction.setContentView(R.layout.options_pop_up);
        TextView close = (TextView) popUpAction.findViewById(R.id.close);
        Button newFriend = (Button) popUpAction.findViewById(R.id.friend);
        Button addEvent = (Button) popUpAction.findViewById(R.id.createEvent);
        Button newGroup = (Button) popUpAction.findViewById(R.id.createGroup);


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMain = new Intent(MainActivity.this, EventActivity.class);
                startMain.putExtra("User", mainUser);
                MainActivity.this.startActivity(startMain);
            }
        });

        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                Intent startMain = new Intent(MainActivity.this, RealLogin.class);
                MainActivity.this.startActivity(startMain);

            }
        });

        newFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startMain = new Intent(MainActivity.this, AddFriends.class);
                startMain.putExtra("User", mainUser);

                MainActivity.this.startActivity(startMain);

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpAction.dismiss();
            }
        });

        popUpAction.show();
    }

    private void initFriendRecycler(){

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.horizontalScrollView);
        FriendRecyclerAdapter adapter = new FriendRecyclerAdapter(this, mainUser.friends);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);

    }

    private void initEventRecycler(ArrayList<Event> list){

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this);
        horizontalLayoutManagaer.setSmoothScrollbarEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.scrollView3);
        EventRecyclerAdapter adapter = new EventRecyclerAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);

    }

    private void initPrivateEventRecycler(ArrayList<Event> list){

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this);
        horizontalLayoutManagaer.setSmoothScrollbarEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.scrollView2);
        EventRecyclerAdapter adapter = new EventRecyclerAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);

    }

    public void totalList(){
        final ArrayList<Event> totalEvents = new ArrayList<>();
        final ArrayList<Event> privateEvents = new ArrayList<>();
        DatabaseReference myReference = FirebaseDatabase.getInstance().getReference("events").child(mainUser.getUsername().toLowerCase());

        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Event event = d.getValue(Event.class);
                    if (event.getPrivacy().equals("Public")) {
                        totalEvents.add(event);
                        initEventRecycler(totalEvents);
                    }
                    else
                    {
                        //Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                        privateEvents.add(event);
                        initPrivateEventRecycler(privateEvents);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for (String u : mainUser.getFriends()){
            DatabaseReference publicFriendReference = FirebaseDatabase.getInstance().getReference("events").child(u.toLowerCase());

            publicFriendReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Event event = d.getValue(Event.class);

                        if(event.getPrivacy().equals("Public")) {
                            totalEvents.add(event);
                            initEventRecycler(totalEvents);
                        }
                        else{
                            if (event.getInvites().contains(mainUser.username.toLowerCase())){
                                //Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                                privateEvents.add(event);
                                initPrivateEventRecycler(privateEvents);

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }

}
