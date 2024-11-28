package com.example.pethousekeeper;

import java.util.Date;

public class Note {
    public int id;
    public String title;
    public String description;
    public Date createdAt;

    public Note() {}

    public Note(int id, String title, String description, Date createdAt){
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    // 您可以根據需要添加 getters 和 setters 方法
}