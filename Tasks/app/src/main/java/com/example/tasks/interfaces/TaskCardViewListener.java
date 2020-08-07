package com.example.tasks.interfaces;

public interface TaskCardViewListener {
    public void onTaskContainerClick(int position, boolean complete);
    public void incompleteTask(int position);
    public void setCompletedTaskListTitle(String s);
    public void completeTask(int position);
}
