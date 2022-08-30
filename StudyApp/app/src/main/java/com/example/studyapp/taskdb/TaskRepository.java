package com.example.studyapp.taskdb;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {
    private TaskDao mTaskDao;
    private LiveData<List<Task>> mAllTasks;
    private LiveData<List<Task>> mCompletedTasks;

    TaskRepository(Application application) {
        TaskDatabase db = TaskDatabase.getDatabase(application);
        mTaskDao = db.TaskDao();
        mAllTasks = mTaskDao.getAllTasks();
        mCompletedTasks = mTaskDao.getUncompletedTasks();
    }

    LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    LiveData<List<Task>> getUncompletedTasks() { return mCompletedTasks; }

    public void insert (Task task) {
        new insertAsyncTask(mTaskDao).execute(task);
    }

    public void update (Task task) { new updateAsyncTask(mTaskDao).execute(task); }

    public void delete (Task task) { new deleteAsyncTask(mTaskDao).execute(task); }

    private static class insertAsyncTask extends android.os.AsyncTask<Task, Void, Void> {

        private TaskDao mAsyncTaskDao;

        insertAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao mAsyncTaskDao;

        updateAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.updateTask(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao mAsyncTaskDao;

        deleteAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.deleteTask(params[0].getId());
            return null;
        }
    }
}
