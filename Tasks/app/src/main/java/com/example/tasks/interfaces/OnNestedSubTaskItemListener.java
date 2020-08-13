package com.example.tasks.interfaces;

public interface OnNestedSubTaskItemListener {
    public void onSubTaskCompleteButtonClick(int subTaskPosition, boolean complete);
    public void onSubTaskDeleteButtonClick(int subTaskPosition, boolean complete);
    public void onSubTaskItemClick(int subTaskPosition, boolean complete);
}
