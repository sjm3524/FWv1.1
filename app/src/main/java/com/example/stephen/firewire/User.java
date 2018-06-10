package com.example.stephen.firewire;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;



import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class User implements Serializable{
    //User[] friends;
    //Event[] events;
    ArrayList<String> friends;
    ArrayList<Event> publicEvents;
    ArrayList<Event> privateEvents;
    ArrayList<Event> hybridEvents;
    //List groups;
    //String name;
    String username;
    String email;

    public User(){
        if (friends == null) {
            friends = new ArrayList<String>();
        }
        if (publicEvents == null) {
            publicEvents = new ArrayList<Event>();
        }
        if (privateEvents == null) {
            privateEvents = new ArrayList<Event>();
        }
        if (hybridEvents == null) {
            hybridEvents = new ArrayList<Event>();
        }
    }

    public User(String username, String email){
        this.username=username;
        this.email=email;
        friends = new ArrayList<>();
        publicEvents = new ArrayList<>();
        privateEvents = new ArrayList<>();
        hybridEvents = new ArrayList<>();
    }

    public void addEvent(Event e){
        if (e.getPrivacy().equals("Public"))
            publicEvents.add(e);
        if (e.getPrivacy().equals("Private"))
            privateEvents.add(e);
    }

    public void addFriend(String friend){
        friends.add(friend);
    }

    public void removeFriend(String friend){
        friends.remove(friend);
    }

    public List<String> getFriends(){
        return friends;
    }

    public String getUsername(){
        return username;
    }

    public List<Event> getPublicEvents(){
        return publicEvents;
    }

    public List<Event> getAllEvents(){
        ArrayList<Event> all = new ArrayList<>();

        all.addAll(publicEvents);
        all.addAll(privateEvents);
        all.addAll(hybridEvents);
        return all;
    }

    public ArrayList<Event> getPrivateEvents() {
        return privateEvents;
    }

    @Override
    public boolean equals(Object obj) {
        User u;
        if (obj instanceof User) {
            u = (User) obj;
            return u.username.equals(username);
        }else return false;

    }
}

