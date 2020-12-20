package com.example.tasks.adapters;

import android.graphics.Paint;
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
import com.example.tasks.interfaces.OnSubTaskItemClickListener;

public class RecyclerAdapterCST extends RecyclerView.Adapter<RecyclerAdapterCST.ViewHolder> {

    Task task;
    OnSubTaskItemClickListener listener;

    public RecyclerAdapterCST(Task task, OnSubTaskItemClickListener listener) {
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
        holder.subTaskTitleTV.setText(task.getCompleteSubTaskAt(position).getTitle());
        holder.subTaskTitleTV.setPaintFlags(holder.subTaskTitleTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        listener.setCompleteHeader(task.getCompleteSubTasksCount() > 0);
        return task.getCompleteSubTasksCount();
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

            completeButton.setImageResource(R.drawable.ic_baseline_check_24);

            subTaskTitleTV.setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), true));
            deleteButton.setOnClickListener(v -> listener.onDeleteButtonClick(getAdapterPosition(), true));
            completeButton.setOnClickListener(v -> listener.onCompleteButtonClick(getAdapterPosition(), true));
        }
    }
}
