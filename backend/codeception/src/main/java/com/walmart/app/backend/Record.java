package com.walmart.app.backend;
public class Record{
    public int x;
    public int y;
    public String username;
    public String time;
    public String filelink;
    public Record(int x,int y, String username, String time, String filelink){
        this.x = x;
        this.y = y;
        this.username = username;
        this.time = time;
        this.filelink = filelink;
    }
}