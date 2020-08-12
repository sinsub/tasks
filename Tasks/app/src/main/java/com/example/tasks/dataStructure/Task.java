package com.example.tasks.dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    private boolean status;                     // true if complete
    private final ArrayList<SubTask> incompleteSubTasks;     // list of subTasks
    private final ArrayList<SubTask> completeSubTasks;
    private LocalDateTime dueTime;              // due time of the task
    private final long timeCreated;             // time when the task was created

    //Constructor
    public Task(String title, String details, LocalDateTime dueTime) {
        this.title = title;
        this.details = details;
        this.dueTime = dueTime;
        this.status = INCOMPLETE;                   // new task defaults to incomplete
        this.incompleteSubTasks = new ArrayList<>();
        this.completeSubTasks = new ArrayList<>();
        timeCreated = System.currentTimeMillis();   // set to time of object creation
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
        if (incompleteSubTasks.size() > 0)
            deepComplete = false;
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

    // Add sub task
    public void addSubTask(SubTask subTask) {
        if (subTask == null)
            throw new IllegalArgumentException("Argument to addSubTask() is null!");
        if (subTask.isComplete()) completeSubTasks.add(subTask);
        else incompleteSubTasks.add(subTask);
    }

    // delete sub task
    public void deleteSubTask(SubTask subTask) {
        if (subTask.isComplete()) completeSubTasks.remove(subTask);
        else incompleteSubTasks.remove(subTask);
    }
    public void deleteIncompleteSubTask(int index) {
        if (index < 0 || index >= incompleteSubTasks.size())
            throw new IllegalArgumentException("Index "+index+" to deleteSubTask is out of range for size "+incompleteSubTasks.size()+"!");
        incompleteSubTasks.remove(index);
    }
    public void deleteCompleteSubTask(int index) {
        if (index < 0 || index >= completeSubTasks.size())
            throw new IllegalArgumentException("Index "+index+" to deleteSubTask is out of range for size "+completeSubTasks.size()+"!");
        completeSubTasks.remove(index);
    }

    // get the sub task at index
    public SubTask getIncompleteSubTaskAt(int index) {
        if (index < 0 || index > incompleteSubTasks.size()) throw new IllegalArgumentException("Index wrong!");
        return incompleteSubTasks.get(index);
    }
    public SubTask getCompleteSubTaskAt(int index) {
        if (index < 0 || index > completeSubTasks.size()) throw new IllegalArgumentException("Index wrong!");
        return completeSubTasks.get(index);
    }

    // get sub tasks count
    public int getIncompleteSubTasksCount() {
        return incompleteSubTasks.size();
    }
    public int getCompleteSubTasksCount() {
        return completeSubTasks.size();
    }

    // complete the sub task at index i
    public void completeSubTaskAt(int index) {
        if (index < 0 || index >= incompleteSubTasks.size()) throw new IllegalArgumentException("Index out of bounds");
        SubTask subTask = incompleteSubTasks.get(index);
        incompleteSubTasks.remove(index);
        subTask.setComplete();
        completeSubTasks.add(subTask);
    }

    // change the title of incomplete task
    public void changeIncompleteSubTaskTitle(int index, String title) {
        incompleteSubTasks.get(index).setTitle(title);
    }

    public void changeCompleteSubTaskTitle(int index, String title) {
        completeSubTasks.get(index).setTitle(title);
    }

    public void incompleteSubTaskAt(int index) {
        if (index < 0 || index >= completeSubTasks.size()) throw new IllegalArgumentException("Index out of bounds");
        SubTask subTask = completeSubTasks.get(index);
        completeSubTasks.remove(index);
        subTask.setComplete();
        incompleteSubTasks.add(subTask);
    }

    public void swapIncompleteSubTasks(int index1, int index2) {
        Collections.swap(incompleteSubTasks, index1, index2);
    }

    public void swapCompleteSubTasks(int index1, int index2) {
        Collections.swap(completeSubTasks, index1, index2);
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

