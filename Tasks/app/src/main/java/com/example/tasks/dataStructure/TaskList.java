package com.example.tasks.dataStructure;


import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskList implements Serializable {
    private final ArrayList<Task> incompleteTasks;
    private final ArrayList<Task> completeTasks;
    private String title;
    private Comparator<Task> taskComparator;

    public TaskList(String title) {
        if (title == null) throw new IllegalArgumentException("Title cannot be null!");
        this.title = title;
        incompleteTasks = new ArrayList<>();
        completeTasks = new ArrayList<>();
        taskComparator = Task.MY_ORDER;
    }


    /*********************************
     * Getters and Setters
     *********************************/

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public void setTaskComparator(Comparator<Task> taskComparator) {
        this.taskComparator = taskComparator;
        sort();
    }
    public Comparator<Task> getTaskComparator() {
        return taskComparator;
    }



    // adds task at :
    //    * the end if taskComparator is set to MY_ORDER, or
    //    * a position so that the list is sorted according to taskComparator
    // returns the position at which task was added
    public int addTask(Task task) {
        if (!task.isComplete()) {
            if (taskComparator == Task.MY_ORDER) {
                incompleteTasks.add(task);
                return incompleteTasks.size() - 1;
            } else {
                int i = 0;
                while (i < incompleteTasks.size()
                        && taskComparator.compare(task, incompleteTasks.get(i)) >= 0) {
                    i++;
                }
                incompleteTasks.add(i, task);
                return i;
            }
        }
        else {
            if (taskComparator == Task.MY_ORDER) {
                completeTasks.add(task);
                return completeTasks.size() - 1;
            } else {
                int i = 0;
                while (i < completeTasks.size()
                        && taskComparator.compare(task, completeTasks.get(i)) >= 0) {
                    i++;
                }
                completeTasks.add(i, task);
                return i;
            }
        }
    }
    public void addTask(Task task, int index) {
        if (task.isComplete()) completeTasks.add(index, task);
        else incompleteTasks.add(index, task);
    }

    // deleting Task
    // returns the index from which the task was deleted
    public int deleteTask(Task task) {
        if (task == null) throw new IllegalArgumentException("Task cannot be null!");
        if (task.isComplete()) {
            int deletedFrom = completeTasks.indexOf(task);
            completeTasks.remove(task);
            return deletedFrom;
        }
        else {
            int deletedFrom = incompleteTasks.indexOf(task);
            incompleteTasks.remove(task);
            return deletedFrom;
        }
    }
    public void deleteCompleteTask(int index) {
        if (index < 0 || index >= completeTasks.size())
            throw new IllegalArgumentException("Index out of bounds!");
        completeTasks.remove(index);
    }
    public void deleteIncompleteTask(int index) {
        if (index < 0 || index >= incompleteTasks.size())
            throw new IllegalArgumentException("Index out of bounds!");
        incompleteTasks.remove(index);
    }
    // delete all completed tasks
    public void deleteCompletedTasks() {
        completeTasks.clear();
    }


    // get a particular task
    public Task getCompletedTask(int index) {
        if (index < 0 || index >= completeTasks.size())
            throw new IllegalArgumentException("Index out of bounds!");
        return completeTasks.get(index);
    }
    public Task getIncompleteTask(int index) {
        if (index < 0 || index >= incompleteTasks.size())
            throw new IllegalArgumentException("Index out of bounds!");
        return incompleteTasks.get(index);
    }


    // Set a completed task as incomplete
    // returns the index at which the task was added into
    public int setTasksIncomplete(int index) {
        Task t = completeTasks.remove(index);
        t.setIncomplete();
        return addTask(t);
    }


    // Set a completed task as incomplete
    // returns the index at which the task was added into
    public int setTasksComplete(int index) {
        Task t = incompleteTasks.remove(index);
        t.setComplete();
        return addTask(t);
    }


    // counts!
    public int incompleteTaskCount() {
        return incompleteTasks.size();
    }
    public int completeTaskCount() {
        return completeTasks.size();
    }


    // sorts the lists
    public void sort() {
        incompleteTasks.sort(taskComparator);
        completeTasks.sort(taskComparator);
    }


    public int getIncompleteTaskPosition(Task task) {
        if (task.isComplete()) throw new IllegalArgumentException("Task is complete!");
        return incompleteTasks.indexOf(task);
    }
    public int getCompleteTaskPosition(Task task) {
        if (!task.isComplete()) throw new IllegalArgumentException("Task is incomplete!");
        return completeTasks.indexOf(task);
    }


    public void swapIncompleteTask(int index1, int index2) {
        Collections.swap(incompleteTasks, index1, index2);
    }
    public void swapCompleteTask(int index1, int index2) {
        Collections.swap(completeTasks, index1, index2);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("completed : ");
        for (Task t : completeTasks) {
            sb.append(" ").append(t.toString()).append(" ");
        }
        sb.append("\nincomplete :");
        for (Task t : incompleteTasks) {
            sb.append(" ").append(t.toString()).append(" ");
        }
        return sb.toString();
    }
}

