package com.example.stephen.firewire;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventActivity extends AppCompatActivity {

    private EditText date, time, title, desc, loc, end, endTime;
    private Spinner spinner;
    private Button create;
    private User mainUser;


    Dialog friendPopUpAction;
    Dialog calendarUpAction;
    Dialog timeUpAction;
    DatabaseReference standardReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        standardReference = FirebaseDatabase.getInstance().getReference();

        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        endTime = findViewById(R.id.editText5);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        create = findViewById(R.id.button);
        loc = findViewById(R.id.editText10);
        spinner = findViewById(R.id.spinner);
        //amPm = findViewById(R.id.spinner2);
        end = findViewById(R.id.editText5);


        Intent intent = getIntent();
        mainUser= (User)intent.getSerializableExtra("User");



// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Event myEvent;
                    Calendar cal = Calendar.getInstance();
                    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                    int year = cal.get(Calendar.YEAR);


                     String[] ifAm = date.getText().toString().split("/");

                     int plusOne = (Integer.parseInt(ifAm[1])+1);

                     if(plusOne>maxDay){
                         plusOne = 1;
                     }

                    Date start = new SimpleDateFormat("yyyy/MM/dd/HH:mm")
                            .parse(year+"/"+date.getText().toString() + "/" + time
                                    .getText().toString());


                    Date end = new SimpleDateFormat("yyyy/MM/dd/HH:mm")
                            .parse(year+"/"+date.getText().toString() + "/" + endTime
                                    .getText().toString());

                     if (end.before(start)){
                         end = new SimpleDateFormat("yyyy/MM/dd/HH:mm")
                                 .parse(year+"/"+ifAm[0]+"/"+plusOne + "/" + endTime
                                         .getText().toString());
                     }

                    String eventType = (String)spinner.getSelectedItem();

                    if (eventType.equals("Public")) {

                        myEvent = new Event(title.getText().toString(), desc.getText().toString(),
                                loc.getText().toString(), start,
                                end, (String) spinner.getSelectedItem(),
                                mainUser.getUsername().toLowerCase());

                        String username = mainUser.getUsername().toLowerCase();

                        //standardReference.child("users").child(mainUser.username.toLowerCase()).setValue(mainUser);
                        standardReference.child("events").child(username).push().setValue(myEvent);
                        Toast.makeText(EventActivity.this, "Event \""+title.getText().toString()+"\" created",
                                Toast.LENGTH_SHORT).show();
                        finish();

                        Intent intent = new Intent(EventActivity.this, MainActivity.class);
                        EventActivity.this.startActivityIfNeeded(intent, 1);

                    }
                    else {
                        friendPopUp(new Event(title.getText().toString(), desc.getText().toString(),
                                loc.getText().toString(), start,
                                end, (String) spinner.getSelectedItem(), MainActivity.invites,
                                mainUser.getUsername().toLowerCase()));



                    }
                    //mainUser.addEvent(myEvent);

                }catch(Exception e){
                    Toast.makeText(EventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });


        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showPopUp(v, date);
                }
            }
        });

        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    timePopUp(v, time);
                }
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePopUp(v,time);
            }
        });

        end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    timePopUp(v, end);
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePopUp(v, end);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v, date);
            }
        });




        calendarUpAction = new Dialog(this);
        timeUpAction = new Dialog(this);
        friendPopUpAction = new Dialog(this);
    }

    public void showPopUp(View v, final EditText date){
        calendarUpAction.setContentView(R.layout.calendar_pop_up);
        final CalendarView calendar = (CalendarView) calendarUpAction.findViewById(R.id.calendarView);
        //long date = calendar.getDate();
        //TextView name = (TextView) friendUpAction.findViewById(R.id.textView11);
        //TextView username = (TextView) friendUpAction.findViewById(R.id.textView12);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Date selectedDate = new Date(year, month, dayOfMonth);
                SimpleDateFormat dateText = new SimpleDateFormat("MM/dd");
                date.setText(dateText.format(selectedDate));
                //Toast.makeText(EventActivity.this, "test", Toast.LENGTH_SHORT).show();
                calendarUpAction.dismiss();
            }
        });



        calendarUpAction.show();
    }


    public void timePopUp(View v, final EditText time){
        timeUpAction.setContentView(R.layout.time_pop_up);
        final TimePicker timepicker = (TimePicker) timeUpAction.findViewById(R.id.timepicker);
        Button ok = (Button) timeUpAction.findViewById(R.id.ok);
        //long date = calendar.getDate();
        //TextView name = (TextView) friendUpAction.findViewById(R.id.textView11);
        //TextView username = (TextView) friendUpAction.findViewById(R.id.textView12);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.setText(timepicker.getHour()+":"+timepicker.getMinute());
                timeUpAction.dismiss();
            }
        });


        timeUpAction.show();
    }

    private void initEventRecycler(ArrayList<String> list, Dialog dialog){


        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(dialog.getContext());

        horizontalLayoutManagaer.setSmoothScrollbarEnabled(true);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycle_view);
        FriendInviteRecyclerAdapter adapter = new FriendInviteRecyclerAdapter(dialog.getContext(), list);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

    }

    public void friendPopUp(final Event event){

        friendPopUpAction.setContentView(R.layout.friend_picker);
        Button ok = (Button) friendPopUpAction.findViewById(R.id.button3);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //event.setInvites(MainActivity.invites);
                //Toast.makeText(EventActivity.this, MainActivity.invites.size() + "",
                //        Toast.LENGTH_SHORT).show();

                event.setInvites(MainActivity.invites);
                standardReference.child("events").child(mainUser.getUsername().toLowerCase()).push().setValue(event);
                Toast.makeText(EventActivity.this, "Event \""+title.getText().toString()+"\" created",
                        Toast.LENGTH_SHORT).show();
                //MainActivity.invites = new ArrayList<>();
                finish();

                Intent intent = new Intent(EventActivity.this, MainActivity.class);
                EventActivity.this.startActivityIfNeeded(intent, 1);

            }
        });
        initEventRecycler(mainUser.friends, friendPopUpAction);
        friendPopUpAction.show();


    }
}
