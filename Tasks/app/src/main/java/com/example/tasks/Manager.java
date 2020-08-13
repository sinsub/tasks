package com.example.tasks;

import android.content.Context;

import com.example.tasks.dataStructure.SubTask;
import com.example.tasks.dataStructure.Task;
import com.example.tasks.dataStructure.TaskList;
import com.example.tasks.dataStructure.User;

import java.time.LocalDateTime;
import java.util.Comparator;

public class Manager {
    public static final int MY_ORDER = 0;
    public static final int CREATED_DATE = 1;
    public static final int DUE_DATE = 3;

    private User user;
    private TaskList openList;
    Context context;

    public Manager(Context context) {
        this.context = context;
        user = FileManager.readUser(context);
        if (user == null) {
            user = new User();
        }
        openList = user.getTaskList(user.getOpenIndex());
        write();
    }

    public void write() {
        FileManager.writeUser(context, user);
    }

    /***************************************************
     *  Functions relating List that is currently open
     ***************************************************/

    public void setOpenList() {
        openList = user.getTaskList(user.getOpenIndex());
        write();
    }

    public void setOpenIndex(int index) {
        user.setOpenIndex(index);
        setOpenList();
        write();
    }

    public void deleteTaskList() {
        user.deleteTaskList();
        setOpenList();
        write();
    }

    public String getListTitle() {
        return openList.getTitle();
    }

    public TaskList getOpenList() {
        return openList;
    }

    public void swapTaskLists(int index1, int index2) {
        if (index1 == 0 || index2 == 0) throw new IllegalArgumentException("Argument to swapTaskLists() has '0'");
        user.swapTaskLists(index1, index2);
        write();
    }

    public void renameOpenList(String title) {
        openList.setTitle(formatString(title));
        write();
    }

    /******************************************
     *  Functions related to tasks in open list
     ******************************************/

    // add task to open list:
    public void quickAddTask(String title) {
        Task task = new Task(formatString(title), "", null);
        openList.addTask(task);
        write();
    }

    public void addTask(String title, String details, LocalDateTime dueDate) {
        Task task = new Task(formatString(title), formatString(details), dueDate);
        openList.addTask(task);
        write();
    }

    public void addTask(Task task) {
        openList.addTask(task);
        write();
    }

    // delete task from open list
    public void deleteIncompleteTask(int index) {
        openList.deleteIncompleteTask(index);
        write();
    }

    public void deleteCompleteTask(int index) {
        openList.deleteCompleteTask(index);
        write();
    }

    public void deleteCompletedTasks() {
        openList.deleteCompletedTasks();
        write();
    }

    public void deleteTask(Task task) {
        if (task == null) throw new IllegalArgumentException("Argument is null");
        openList.deleteTask(task);
        write();
    }

    public void changeStatusOfTask(Task task) {
        if (task == null) throw new IllegalArgumentException("Argument is null");
        if (task.isComplete()) {
            int index = openList.getCompleteTaskPosition(task);
            setTaskIncomplete(index);
        } else {
            int index = openList.getIncompleteTaskPosition(task);
            setTaskComplete(index);
        }
        write();
    }

    public void changeTaskTitle(Task task, String title) {
        task.setTitle(title);
        write();
    }

    public void changeTaskDetails(Task task, String details) {
        task.setDetails(details);
        write();
    }


    // change a completed task to incomplete
    public void setTaskIncomplete(int index) {
        openList.setTasksIncomplete(index);
        write();
    }

    public void undoSetTaskIncomplete(Task task, int index) {
        openList.undoSetTaskIncomplete(task, index);
        write();
    }

    public boolean setTaskComplete(int index) {
        boolean ret = openList.setTasksComplete(index);
        write();
        return ret;
    }

    public void undoSetTaskComplete(Task task, int index) {
        openList.undoSetTaskComplete(task, index);
        write();
    }

    public String getIncompleteTaskTitle(int index) {
        return openList.getIncompleteTask(index).getTitle();
    }

    public String getCompleteTaskTitle(int index) {
        return openList.getCompletedTask(index).getTitle();
    }

    public void swapIncompleteTasks(int index1, int index2) {
        openList.swapIncompleteTask(index1, index2);
        write();
    }

    public void swapCompleteTasks(int index1, int index2) {
        openList.swapCompleteTask(index1, index2);
        write();
    }


    public void moveTaskToList(Task task, int position) {
        if (task == null) throw new IllegalArgumentException("Task cannot be null!");
        if (position == getOpenListPosition()) return;
        deleteTask(task);
        setOpenIndex(position);
        addTask(task);
        write();
    }

    public int getOpenListComparator() {
        if (openList.getTaskComparator().getClass().equals(Task.MY_ORDER.getClass())) {
            return MY_ORDER;
        } else if (openList.getTaskComparator().getClass().equals(Task.CREATED_TIME_ORDER.getClass())) {
            return CREATED_DATE;
        } else {
            return 1;
        }
    }

    public void setOpenListComparator(int comparator) {
        if (comparator == MY_ORDER) {
            openList.setTaskComparator(Task.MY_ORDER);
        } else if (comparator == CREATED_DATE) {
            openList.setTaskComparator(Task.CREATED_TIME_ORDER);
        }
        write();
    }


    /***************************************************
     * Methods for Sub Tasks
     **************************************************/

    public void addSubTask(String title, Task task) {
        if (title == null || task == null) throw new IllegalArgumentException("Null argument!");
        SubTask subTask = new SubTask(title, null);
        task.addSubTask(subTask);
        write();
    }

    public SubTask completeSubTaskOf(Task task, int index) {
        SubTask subTask = task.getIncompleteSubTaskAt(index);
        task.completeSubTaskAt(index);
        write();
        return subTask;
    }

    public void deleteIncompleteSubTask(Task task, int index) {
        task.deleteIncompleteSubTask(index);
        write();
    }

    public void undoDeleteIncompleteSubTask(Task task, SubTask subTask,int index) {
        task.undoDeleteIncompleteSubTask(subTask, index);
        write();
    }

    public void changeIncompleteSubTaskTitle(Task task, int index, String title) {
        task.changeIncompleteSubTaskTitle(index, title);
        write();
    }

    public void changeCompleteSubTaskTitle(Task task, int index, String title) {
        task.changeCompleteSubTaskTitle(index, title);
        write();
    }

    public void deleteCompleteSubTask(Task task, int index) {
        task.deleteCompleteSubTask(index);
        write();
    }

    public void undoDeleteCompleteSubTask(Task task, SubTask subTask,int index) {
        task.undoDeleteCompleteSubTask(subTask, index);
        write();
    }

    public SubTask incompleteSubTaskOf(Task task, int index) {
        SubTask subTask = task.getCompleteSubTaskAt(index);
        task.incompleteSubTaskAt(index);
        write();
        return subTask;
    }

    public void undoIncompleteSubTaskOf(Task task, SubTask subTask, int position) {
        task.undoIncompleteSubTask(subTask, position);
        write();
    }

    public void undoCompleteSubTaskOf(Task task, SubTask subTask, int position) {
        task.undoCompleteSubTask(subTask, position);
        write();
    }

    public void swapIncompleteSubTasks(Task task, int index1, int index2) {
        task.swapIncompleteSubTasks(index1, index2);
        write();
    }

    public void swapCompleteSubTasks(Task task, int index1, int index2) {
        task.swapCompleteSubTasks(index1, index2);
        write();
    }

    /*********************************************
     *  functions relating all Lists
     *********************************************/

    public User getUser() {
        return user;
    }

    public int getOpenListPosition() {
        return user.getOpenIndex();
    }

    public void addNewList(String title) {
        TaskList newList = new TaskList(formatString(title));
        user.addTaskList(newList);
        write();
    }

    private String formatString(String title) {
        return title.trim();
    }
}
