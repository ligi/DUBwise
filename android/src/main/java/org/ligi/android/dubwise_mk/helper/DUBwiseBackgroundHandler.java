package org.ligi.android.dubwise_mk.helper;

import java.util.Vector;

public class DUBwiseBackgroundHandler {

    private Vector<DUBwiseBackgroundTask> tasks;

    public DUBwiseBackgroundHandler() {
        tasks = new Vector<DUBwiseBackgroundTask>();
    }

    /**
     * add and start a background task
     *
     * @param task - the task to add
     */
    public void addAndStartTask(DUBwiseBackgroundTask task) {
        tasks.add(task);
        task.start();
    }

    /**
     * stop all tasks
     */
    public void stopAll() {
        for (DUBwiseBackgroundTask task : tasks)
            task.stop();
    }

    public Vector<DUBwiseBackgroundTask> getBackgroundTasks() {
        return tasks;
    }

    /* singleton stuff */
    private static DUBwiseBackgroundHandler instance = null;

    public static DUBwiseBackgroundHandler getInstance() {
        if (instance == null)
            instance = new DUBwiseBackgroundHandler();
        return instance;
    }

}
