package com.example.studyapp.taskdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "task_table")
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "timeProgress")
    private Float timeProgress;

    @ColumnInfo(name = "isComplete")
    private int isComplete;

    //Constructor
    public Task(
            String title,
            String description,
            Float timeProgress,
            int isComplete
    ) {
        this.title = title;
        this.description = description;
        this.timeProgress = timeProgress;
        this.isComplete = isComplete;
    }

    @Ignore
    //ID Constructor
    public Task(
            int id,
            String title,
            String description,
            Float timeProgress,
            int isComplete
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeProgress = timeProgress;
        this.isComplete = isComplete;
    }

    //Getters & Setters

    @NonNull
    public Integer getId() { return id; }

    public void setId(@NonNull Integer id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Float getTimeProgress() { return timeProgress; }

    public void setTimeProgress(Float timeProgress) {
        this.timeProgress = timeProgress;
    }

    public int getIsComplete() { return isComplete; }

    public void setIsComplete(int isComplete) { this.isComplete = isComplete; }
}
