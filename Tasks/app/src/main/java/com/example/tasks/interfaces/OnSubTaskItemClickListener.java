package com.example.tasks.interfaces;

public interface OnSubTaskItemClickListener {
    public void onCompleteButtonClick(int position);
    public void onDeleteButtonClick(int position);
    public void onTitleChange(int position, String title);
}
