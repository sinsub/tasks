package com.example.tasks.dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class Task implements Serializable {
    public static final Comparator<Task> COMPLETE_ORDER = new Completeness();
    public static final Comparator<Task> DUE_DATE_ORDER = new DueDate();
    public static final Comparator<Task> CREATED_TIME_ORDER = new CreatedTime();
    public static final Comparator<Task> MY_ORDER = new MyOrder();

    private static final boolean COMPLETE = true;
    private static final boolean INCOMPLETE = false;

    private String title;                       // title of the task
    private String details;                     // details of the task
    private boolean status;                     // true if status
    private final ArrayList<SubTask> subTasks;     // list of subTasks
    private LocalDateTime dueTime;              // due time of the task
    private final long timeCreated;             // time when the task was created

    //Constructor
    public Task(String title, String details, LocalDateTime dueTime) {
        this.title = title;
        this.details = details;
        this.dueTime = dueTime;
        this.status = INCOMPLETE;                   // new task defaults to incomplete
        this.subTasks = new ArrayList<>();
        timeCreated = System.currentTimeMillis();   // set to time of object creation
    }

    @Override
    public String toString() {
        return title;
    }

    /*************************************************
     * Getters and Setters
     *************************************************/

    public boolean isComplete() {
        return status == COMPLETE;
    }
    public boolean setComplete() {
        status = COMPLETE;
        boolean deepComplete = true;
        for (SubTask st : subTasks) {
            if (!st.isComplete()) {
//                st.setComplete();
                deepComplete = false;
            }
        }
        return deepComplete;
    }
    public void setIncomplete() {
        status = INCOMPLETE;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }
    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }

    public long getTimeCreated() {
        return timeCreated;
    }


    /*************************************************
     * functions related to SubTasks
     *************************************************/

    public void addSubTask(SubTask subTask) {
        if (subTasks == null)
            throw new IllegalArgumentException("Argument to addSubTask() is null!");
        subTasks.add(subTask);
    }

    public void deleteSubTask(SubTask subTask) {
        if (subTask == null)
            throw new IllegalArgumentException("Argument to deleteSubTask() is null!");
        subTasks.remove(subTask);
    }

    public void deleteSubTask(int index) {
        if (index < 0 || index >= subTasks.size())
            throw new IllegalArgumentException("Index "+index+" to deleteSubTask is out of range for size "+subTasks.size()+"!");
        subTasks.remove(index);
    }

    public void deleteCompletedSubTasks() {
        while (true) {
            int index = -1;
            for (int i = 0 ; i < subTasks.size(); i++) {
                if (subTasks.get(i).isComplete()) {
                    index = i;
                    break;
                }
            }
            if (index == -1)
                return;
            deleteSubTask(index);
        }
    }

    /***************************************************
     *  Comparators
     ***************************************************/

    public static class Completeness implements Comparator<Task>, Serializable{

        @Override
        public int compare(Task o1, Task o2) {
            if (o1.isComplete() && !o2.isComplete()) return +1;
            if (!o1.isComplete() && o2.isComplete()) return -1;
            return 0;
        }
    }

    public static class DueDate implements Comparator<Task>, Serializable {

        @Override
        public int compare(Task o1, Task o2) {
            if (o1.dueTime == null && o2.dueTime == null)
                return 0;
            if (o1.dueTime == null)
                return +1;
            if (o2.dueTime == null)
                return -1;
            return o1.dueTime.compareTo(o2.dueTime);
        }
    }

    public static class CreatedTime implements Comparator<Task>, Serializable {

        @Override
        public int compare(Task o1, Task o2) {
            return Long.compare(o1.getTimeCreated(), o2.getTimeCreated());
        }
    }

    public static class MyOrder implements Comparator<Task>, Serializable {
        @Override
        public int compare(Task o1, Task o2) {
            return 0;
        }
    }


}

