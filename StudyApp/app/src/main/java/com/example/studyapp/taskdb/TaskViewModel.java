package com.example.studyapp.taskdb;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository mRepository;

    private LiveData<List<Task>> mAllTasks;
    private LiveData<List<Task>> mCompletedTasks;

    public TaskViewModel (Application application) {
        super(application);
        mRepository = new TaskRepository(application);
        mAllTasks = mRepository.getAllTasks();
        mCompletedTasks = mRepository.getUncompletedTasks();
    }

    public LiveData<List<Task>> getAllTasks() { return mAllTasks; }

    public LiveData<List<Task>> getUncompletedTasks() { return mCompletedTasks; }

    public void insert(Task task) {
        mRepository.insert(task);
    }

    public void update(Task task) {
        mRepository.update(task);
    }

    public void delete(Task task) {
        mRepository.delete(task);
    }
}
