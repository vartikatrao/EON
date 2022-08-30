package com.example.studyapp.taskdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Update
    void updateTask(Task task);

    @Query("DELETE FROM task_table WHERE id = :id")
    void deleteTask(int id);

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE isComplete = 0 ORDER BY id ASC")
    LiveData<List<Task>> getUncompletedTasks();
}
