package com.example.stephen.firewire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Event implements Serializable{

    private String name, desc, loc,privacy, user;

    private Date startDate, endDate;

    private ArrayList<String> invites;


    public Event(){
        if (invites ==null){
            invites =new ArrayList<>();
        }

    }

    public Event(String name, String desc, String loc, Date date, Date end, String privacy, String user) {
        this.name = name;
        this.desc = desc;
        this.loc = loc;
        this.privacy = privacy;
        this.startDate = date;
        this.user = user;
        this.endDate=end;

    }
    public Event(String name, String desc, String loc, Date date, Date end, String privacy, ArrayList<String> invites, String user) {
        this.name = name;
        this.desc = desc;
        this.loc = loc;
        this.privacy = privacy;
        this.startDate = date;
        this.invites = invites;
        this.user = user;
        this.endDate=end;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getLoc() {
        return loc;
    }

    public String getUser() {
        return user;
    }

    public String getPrivacy() {
        return privacy;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public ArrayList<String> getInvites() {
        return invites;
    }

    public void setInvites(ArrayList<String> invites) {
        this.invites = invites;
    }
}


