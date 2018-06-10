package com.example.stephen.firewire;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    private ArrayList<Event> events;
    Context context;

    public EventRecyclerAdapter(Context context, ArrayList<Event> events) {
        this.events = events;
        this.context=context;
    }

    @NonNull
    @Override
    public EventRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_layout_icon, parent, false);
        EventRecyclerAdapter.ViewHolder holder = new EventRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapter.ViewHolder holder, final int position) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d");

        holder.name.setText(events.get(position).getName());
        holder.time.setText(timeFormat.format(events.get(position).getStartDate())+"-"+
                                    timeFormat.format(events.get(position).getEndDate()));

        holder.month.setText(dateFormat.format(events.get(position).getStartDate()));
        holder.description.setText(events.get(position).getDesc());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, events.get(position).getName(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView description;
        TextView name, time, month;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView6);
            time = itemView.findViewById(R.id.icon_date);
            month = itemView.findViewById(R.id.month);
            description = itemView.findViewById(R.id.description);
            parentLayout = itemView.findViewById(R.id.parentlayout);

        }
    }
}
