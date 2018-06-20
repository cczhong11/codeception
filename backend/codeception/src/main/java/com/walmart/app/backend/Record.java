package com.walmart.app.backend;
public class Record{
    public float x;
    public float y;
    public String username;
    public String time;
    public String filelink;
    public String description;
    public String Thumbfilelink;
    public Record(float x,float y, String username, String time, String filelink,String Tfilelink,String description){
        this.x = x;
        this.y = y;
        this.username = username;
        this.time = time;
        this.description = description;
        this.filelink = filelink;
        this.Thumbfilelink = Tfilelink;
    }
}