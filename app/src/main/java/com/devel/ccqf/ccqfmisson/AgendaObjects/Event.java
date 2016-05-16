package com.devel.ccqf.ccqfmisson.AgendaObjects;

import java.util.Date;

/**
 * Created by jo on 5/16/16.
 */
public class Event {
    protected int id;
    protected String title;
    protected String time;
    long DTStart;
    long DTEnd;

    public Event( String title, String time) {
        this.title = title;
        this.time = time;
    }
    public Event(String title, String DTStart, String DTEnd){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", time=" + time +
                '}';
    }
}