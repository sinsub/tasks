package com.example.tasks.dataStructure;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SubTask implements Serializable {
    private static final boolean COMPLETE = true;
    private static final boolean INCOMPLETE = false;

    private String title;                       // title of the task
    private boolean status;                     // true if complete
    private final long timeCreated;             // time when the task was created

    //Constructor
    public SubTask(String title, LocalDateTime dueTime) {
        this.title = title;
        this.status = INCOMPLETE;              // new task defaults to incomplete
        timeCreated = System.currentTimeMillis();   // set to time of object creation
    }

    /*************************************************
     * Getters and Setters
     *************************************************/

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isComplete() {
        return status == COMPLETE;
    }
    public void setComplete() {
        status = COMPLETE;
    }
    public void setIncomplete() {
        status = INCOMPLETE;
    }
}
