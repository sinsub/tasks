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
    public static final int DUE_DATE = 2;

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
    }

    public void setOpenIndex(int index) {
        user.setOpenIndex(index);
        setOpenList();
        write();
    }

    // delete the open list
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

    public String getIncompleteTaskTitle(int index) {
        return openList.getIncompleteTask(index).getTitle();
    }
    public String getCompleteTaskTitle(int index) {
        return openList.getCompletedTask(index).getTitle();
    }
    public void changeTaskTitle(Task task, String title) {
        task.setTitle(formatString(title));
        write();
    }
    public void changeTaskDetails(Task task, String details) {
        task.setDetails(formatString(details));
        write();
    }



    // add task to open list
    // returns position at which task was added
    public int addTask(String title, String details, LocalDateTime dueDate, boolean timeSet) {
        Task task = new Task(formatString(title), formatString(details), dueDate, timeSet);
        int index = openList.addTask(task);
        write();
        return index;
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

    // change a completed task to incomplete
    public int setTaskIncomplete(int index) {
        int insertedAt = openList.setTasksIncomplete(index);
        write();
        return insertedAt;
    }
    public int undoSetTaskIncomplete(Task task, int index) {
        if (task.isComplete()) throw new IllegalArgumentException("Task should be incomplete");
        int deletedFrom = openList.deleteTask(task);
        task.setComplete();
        openList.addTask(task, index);
        write();
        return deletedFrom;
    }

    public int setTaskComplete(int index) {
        int ret = openList.setTasksComplete(index);
        write();
        return ret;
    }
    public int undoSetTaskComplete(Task task, int index) {
        if (!task.isComplete()) throw new IllegalArgumentException("Task should be complete");
        int deletedFrom = openList.deleteTask(task);
        task.setIncomplete();
        openList.addTask(task, index);
        write();
        return deletedFrom;
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
            return DUE_DATE;
        }
    }

    public void setOpenListComparator(int comparator) {
        if (comparator == MY_ORDER) {
            openList.setTaskComparator(Task.MY_ORDER);
        } else if (comparator == CREATED_DATE) {
            openList.setTaskComparator(Task.CREATED_TIME_ORDER);
        } else if (comparator == DUE_DATE) {
            openList.setTaskComparator(Task.DUE_DATE_ORDER);
        }
        write();
    }


    /***************************************************
     * Methods for Sub Tasks
     **************************************************/

    public int addSubTask(String title, Task task) {
        if (title == null || task == null) throw new IllegalArgumentException("Null argument!");
        SubTask subTask = new SubTask(title, null);
        int insertedAt = task.addSubTask(subTask);
        write();
        return insertedAt;
    }


    public int completeSubTaskOf(Task task, int index) {
        int insertedAt = task.completeSubTaskAt(index);
        write();
        return insertedAt;
    }
    public int undoCompleteSubTaskOf(Task task, SubTask subTask, int position) {
        int deletedFrom = task.deleteSubTask(subTask);
        subTask.setIncomplete();
        task.addSubTask(subTask, position);
        write();
        return deletedFrom;
    }

    public int incompleteSubTaskOf(Task task, int index) {
        int insertedAt =  task.incompleteSubTaskAt(index);
        write();
        return insertedAt;
    }
    public int undoIncompleteSubTaskOf(Task task, SubTask subTask, int position) {
        int deletedFrom = task.deleteSubTask(subTask);
        subTask.setComplete();
        task.addSubTask(subTask, position);
        write();
        return deletedFrom;
    }


    public void deleteIncompleteSubTask(Task task, int index) {
        task.deleteIncompleteSubTask(index);
        write();
    }
    public void undoDeleteIncompleteSubTask(Task task, SubTask subTask,int index) {
        task.addSubTask(subTask, index);
        write();
    }

    public void deleteCompleteSubTask(Task task, int index) {
        task.deleteCompleteSubTask(index);
        write();
    }
    public void undoDeleteCompleteSubTask(Task task, SubTask subTask,int index) {
        task.addSubTask(subTask, index);
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

    public int addNewList(String title) {
        TaskList newList = new TaskList(formatString(title));
        int insertedAt = user.addTaskList(newList);
        write();
        return insertedAt;
    }

    private String formatString(String title) {
        return title.trim();
    }
}
