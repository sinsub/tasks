package com.example.tasks.dataStructure;


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

    // adding new Tasks
    public void addTask(Task task) {
        if (!task.isComplete()) incompleteTasks.add(task);
        else completeTasks.add(task);
        sort();
    }

    // deleting Tasks
    public void deleteTask(Task task) {
        if (task == null) throw new IllegalArgumentException("Task cannot be null!");
        if (task.isComplete()) completeTasks.remove(task);
        else incompleteTasks.remove(task);
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
    public void setTasksIncomplete(int index) {
        if (index < 0 || index >= completeTasks.size())
            throw new IllegalArgumentException("Index out of bounds!");
        Task t = completeTasks.get(index);
        completeTasks.remove(index);
        t.setIncomplete();
        incompleteTasks.add(t);
        sort();
    }

    // Just changes the list they belong too ---> CHANGE THIS!!!!
    public boolean setTasksComplete(int index) {
        if (index < 0 || index >= incompleteTasks.size())
            throw new IllegalArgumentException("Index out of bounds!");
        Task t = incompleteTasks.get(index);
        incompleteTasks.remove(index);
        boolean ret = t.setComplete();
        completeTasks.add(t);
        return ret;
    }


    // Getter abs setter for title!
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void swapIncompleteTask(int index1, int index2) {
        if (index1 < 0 || index1 >= incompleteTasks.size())
            throw new IllegalArgumentException("Index out of bounds!");
        if (index2 < 0 || index2 >= incompleteTasks.size())
            throw new IllegalArgumentException("Index out of bounds!");
        Collections.swap(incompleteTasks, index1, index2);
    }


    // sort the incomplete list!
    public void sort() {
        incompleteTasks.sort(taskComparator);
    }

    public void setTaskComparator(Comparator<Task> taskComparator) {
        this.taskComparator = taskComparator;
        sort();
    }

    public Comparator<Task> getTaskComparator() {
        return taskComparator;
    }

    // counts!
    public int incompleteTaskCount() {
        return incompleteTasks.size();
    }

    public int completeTaskCount() {
        return completeTasks.size();
    }

    public int getIncompleteTaskPosition(Task task) {
        if (task.isComplete()) throw new IllegalArgumentException("Task is complete!");
        return incompleteTasks.indexOf(task);
    }

    public int getCompleteTaskPosition(Task task) {
        if (!task.isComplete()) throw new IllegalArgumentException("Task is incomplete!");
        return completeTasks.indexOf(task);
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

