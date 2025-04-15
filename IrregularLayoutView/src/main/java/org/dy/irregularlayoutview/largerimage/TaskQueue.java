package org.dy.irregularlayoutview.largerimage;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * create by LuckyJayce at 2017/3/20
 */
public class TaskQueue {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        task.executeOnExecutor(executorService);
    }

    public void cancelTask(Task task) {
        if (task == null) {
            return;
        }
        task.cancel(true);
    }

    public static abstract class Task extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected final void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            onCancelled();
        }

        protected void onCancelled() {
            
        }

        @Override
        protected final Void doInBackground(Void... params) {
            doInBackground();
            return null;
        }

        protected abstract void doInBackground();

        @Override
        protected final void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onPostExecute();
        }

        protected void onPostExecute() {
        }
    }
}
