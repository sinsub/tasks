package com.example.tasks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasks.R;
import com.example.tasks.dataStructure.Task;
import com.example.tasks.interfaces.OnNestedSubTaskItemListener;

public class RecyclerAdapterNIST extends RecyclerView.Adapter<RecyclerAdapterNIST.ViewHolder> {

    Task task;
    OnNestedSubTaskItemListener listener;

    public RecyclerAdapterNIST(Task task, OnNestedSubTaskItemListener listener) {
        this.task = task;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_task_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.subTaskTitleTV.setText(task.getIncompleteSubTaskAt(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return task.getIncompleteSubTasksCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton completeButton;
        TextView subTaskTitleTV;
        ImageButton deleteButton;
        ConstraintLayout itemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            completeButton = (ImageButton) itemView.findViewById(R.id.sub_task_item_complete_image_button);
            deleteButton = (ImageButton) itemView.findViewById(R.id.sub_task_item_delete_image_button);
            subTaskTitleTV = (TextView) itemView.findViewById(R.id.sub_task_item_title_tv);
            itemContainer = (ConstraintLayout) itemView.findViewById(R.id.sub_task_item_container);

            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSubTaskCompleteButtonClick(getAdapterPosition(), false);
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSubTaskDeleteButtonClick(getAdapterPosition(), false);
                }
            });
            subTaskTitleTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSubTaskItemClick(getAdapterPosition(), false);
                }
            });
        }
    }
}
