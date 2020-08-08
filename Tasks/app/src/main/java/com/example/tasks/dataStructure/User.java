package com.example.tasks.dataStructure;

import com.example.tasks.dataStructure.TaskList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class User implements Serializable {
    private final ArrayList<TaskList> taskLists;
    private int openIndex;
    private boolean darkMode;

    public User() {
        taskLists = new ArrayList<>();
        taskLists.add(new TaskList("ToDo List"));   // default TaskList
        openIndex = 0;
        darkMode = true;
    }

    public void addTaskList(TaskList taskList) {
        if (taskList == null) throw new IllegalArgumentException("TaskList cannot be null!");
        taskLists.add(taskList);
        openIndex = taskLists.indexOf(taskList);
    }

    public void deleteTaskList() {
        if (openIndex < 0 || openIndex >= taskLists.size())
            throw new IllegalArgumentException("OpenIndex out of bounds while deleting!");
        if (openIndex == 0) return;                         // cannot remove default TaskList
        taskLists.remove(openIndex);
        openIndex = 0;
    }

    public TaskList getTaskList(int index) {
        if (index < 0 || index >= taskLists.size()) throw new IllegalArgumentException("Index out of bounds!");
        return taskLists.get(index);
    }

    public String getTaskListTitle(int index) {
        if (index < 0 || index >= taskLists.size()) throw new IllegalArgumentException("Index out of bounds!");
        return taskLists.get(index).getTitle();
    }

    public int getTaskListCount() {
        return taskLists.size();
    }

    public int getOpenIndex() {
        return openIndex;
    }

    public void setOpenIndex(int index) {
        if (index < 0 || index >= taskLists.size()) throw new IllegalArgumentException("Index out of bounds!");
        openIndex = index;
    }

    public void swapTaskLists(int index1, int index2) {
        if (index1 < 0 || index1 >= taskLists.size()) throw new IllegalArgumentException("Index out of bounds!");
        if (index2 < 0 || index2 >= taskLists.size()) throw new IllegalArgumentException("Index out of bounds!");
        Collections.swap(taskLists, index1, index2);
    }

    public int numberOfIncompleteTasks(int index) {
        if (index < 0 || index >= taskLists.size()) throw new IllegalArgumentException("Index out of bounds!");
        return taskLists.get(index).incompleteTaskCount();
    }

    public void setDarkModeOn() {
        this.darkMode = true;
    }

    public void setDarkModeOff() {
        this.darkMode = false;
    }

    public boolean isDarkModeOn() {
        return darkMode;
    }

    @Override
    public String toString() {
        return Integer.toString(openIndex);
    }

}

