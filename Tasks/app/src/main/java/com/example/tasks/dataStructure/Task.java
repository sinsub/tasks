package com.example.tasks.dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Task implements Serializable {
    public static final Comparator<Task> DUE_DATE_ORDER = new DueDate();
    public static final Comparator<Task> CREATED_TIME_ORDER = new CreatedTime();
    public static final Comparator<Task> MY_ORDER = new MyOrder();

    private static final boolean COMPLETE = true;
    private static final boolean INCOMPLETE = false;

    private String title;                       // title of the task
    private String details;                     // details of the task
    private boolean status;                     // true if complete
    private final ArrayList<SubTask> incompleteSubTasks;
    private final ArrayList<SubTask> completeSubTasks;
    private LocalDateTime dueDateTime;              // due date and time of the task
    private boolean timeSet;                    // due time was set or not
    private final long timeCreated;             // time when the task was created

    //Constructor
    public Task(String title, String details, LocalDateTime dueDateTime, boolean timeSet) {
        this.title = title;
        this.details = details;
        this.dueDateTime = dueDateTime;
        this.timeSet = timeSet;
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
    // returns true if no incomplete sub tasks exist
    public boolean setComplete() {
        status = COMPLETE;
        boolean deepComplete = true;    // true if all sub tasks are complete
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


    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }
    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }
    public String getFormattedDueDateTime() {
        if (dueDateTime == null) return "";
        String ret = "";
        // check if today, tomorrow or yesterday :
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        LocalDateTime tomorrow = today.plusDays(1);
        if (today.truncatedTo(ChronoUnit.DAYS).equals(dueDateTime.truncatedTo(ChronoUnit.DAYS)))
            ret = "Today";
        else if (yesterday.truncatedTo(ChronoUnit.DAYS).equals(dueDateTime.truncatedTo(ChronoUnit.DAYS)))
            ret = "Yesterday";
        else if (tomorrow.truncatedTo(ChronoUnit.DAYS).equals(dueDateTime.truncatedTo(ChronoUnit.DAYS)))
            ret = "Tomorrow";
        else if (today.getYear() != dueDateTime.getYear())
            ret = dueDateTime.format(DateTimeFormatter.ofPattern("EEE, dd MMM yy"));
        else if (today.getMonth() != dueDateTime.getMonth())
            ret = dueDateTime.format(DateTimeFormatter.ofPattern("EEE, dd MMM"));
        else
            ret = dueDateTime.format(DateTimeFormatter.ofPattern("EEE, dd"));
        // if timeSet add it to string
        if (timeSet)
            ret = ret + dueDateTime.format(DateTimeFormatter.ofPattern(", HH:mm"));
        return ret;
    }
    public String getFormattedDue_Date() {
        if (dueDateTime == null) return "";
        return dueDateTime.format(DateTimeFormatter.ofPattern("EEE, dd MMM yy"));
    }
    public String getFormattedDue_Time() {
        if (!timeSet) return "";
        return dueDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }


    public boolean isTimeSet() {
        return timeSet;
    }
    public void setTimeSet(boolean timeSet) {
        this.timeSet = timeSet;
    }


    public long getTimeCreated() {
        return timeCreated;
    }

    // if no date is set in in future
    public boolean isInPast() {
        if (!timeSet)
            return dueDateTime != null && dueDateTime.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
        return dueDateTime != null && dueDateTime.isBefore(LocalDateTime.now());
    }


    /*************************************************
     * functions related to SubTasks
     *************************************************/

    // adding sub task to the task
    // returns index at which sub task was added
    public int addSubTask(SubTask subTask) {
        if (subTask == null)
            throw new IllegalArgumentException("Argument to addSubTask() is null!");
        if (subTask.isComplete()) {
            completeSubTasks.add(subTask);
            return completeSubTasks.size() - 1;
        }
        else {
            incompleteSubTasks.add(subTask);
            return incompleteSubTasks.size() - 1;
        }
    }
    public void addSubTask(SubTask subTask, int index) {
        if (subTask == null)
            throw new IllegalArgumentException("Argument to addSubTask() is null!");
        if (subTask.isComplete()) {
            if (index < 0 || index > completeSubTasks.size())
                throw new IllegalArgumentException("Index " + index + " to addSubTask is" +
                        " out of range for size " + completeSubTasks.size());
            completeSubTasks.add(index, subTask);
        } else {
            if (index < 0 || index > incompleteSubTasks.size())
                throw new IllegalArgumentException("Index " + index + " to addSubTask is" +
                        " out of range for size " + incompleteSubTasks.size());
            incompleteSubTasks.add(index, subTask);
        }
    }


    // deleting sub task from the task:
    // returns index from which task was deleted
    public int deleteSubTask(SubTask subTask) {
        int deleteFrom;
        if (subTask.isComplete()) {
            deleteFrom = completeSubTasks.indexOf(subTask);
            completeSubTasks.remove(subTask);
        }
        else {
            deleteFrom = incompleteSubTasks.indexOf(subTask);
            incompleteSubTasks.remove(subTask);
        }
        return deleteFrom;
    }
    public SubTask deleteIncompleteSubTask(int index) {
        if (index < 0 || index >= incompleteSubTasks.size())
            throw new IllegalArgumentException("Index "+index+" to deleteSubTask is out of range for size "+incompleteSubTasks.size()+"!");
        return incompleteSubTasks.remove(index);
    }
    public SubTask deleteCompleteSubTask(int index) {
        if (index < 0 || index >= completeSubTasks.size())
            throw new IllegalArgumentException("Index "+index+" to deleteSubTask is out of range for size "+completeSubTasks.size()+"!");
        return completeSubTasks.remove(index);
    }


    // get sub task
    public SubTask getIncompleteSubTaskAt(int index) {
        return incompleteSubTasks.get(index);
    }
    public SubTask getCompleteSubTaskAt(int index) {
        return completeSubTasks.get(index);
    }


    // complete the sub task at index
    public int completeSubTaskAt(int index) {
        SubTask subTask = deleteIncompleteSubTask(index);
        subTask.setComplete();
        return addSubTask(subTask);
    }
    // incomplete the sub task at index
    public int incompleteSubTaskAt(int index) {
        SubTask subTask = deleteCompleteSubTask(index);
        subTask.setIncomplete();
        return addSubTask(subTask);
    }


    // get sub task count
    public int getIncompleteSubTasksCount() {
        return incompleteSubTasks.size();
    }
    public int getCompleteSubTasksCount() {
        return completeSubTasks.size();
    }


    // change the title of sub task
    public void changeIncompleteSubTaskTitle(int index, String title) {
        incompleteSubTasks.get(index).setTitle(title);
    }
    public void changeCompleteSubTaskTitle(int index, String title) {
        completeSubTasks.get(index).setTitle(title);
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

    public static class DueDate implements Comparator<Task>, Serializable {
        @Override
        public int compare(Task o1, Task o2) {
            if (o1.dueDateTime == null && o2.dueDateTime == null)
                return 0;
            if (o1.dueDateTime == null)
                return +1;
            if (o2.dueDateTime == null)
                return -1;
            if (o1.dueDateTime.truncatedTo(ChronoUnit.DAYS).equals(o2.dueDateTime.truncatedTo(ChronoUnit.DAYS))) {
                if (!o1.timeSet && !o2.timeSet) return 0;
                else if (!o1.timeSet) return +1;
                else if (!o2.timeSet) return -1;
            }
            return o1.dueDateTime.compareTo(o2.dueDateTime);
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

