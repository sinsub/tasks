package com.example.tasks.interfaces;

public interface OnSubTaskItemClickListener {
    public void onCompleteButtonClick(int position, boolean complete);
    public void onDeleteButtonClick(int position, boolean complete);
    public void onItemClick(int position, boolean complete);
    public void setCompleteHeader(boolean has);
}
