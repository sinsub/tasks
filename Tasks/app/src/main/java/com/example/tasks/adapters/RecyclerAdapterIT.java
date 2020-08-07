package com.example.tasks.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasks.R;
import com.example.tasks.dataStructure.Task;
import com.example.tasks.dataStructure.TaskList;
import com.example.tasks.interfaces.TaskCardViewListener;

public class RecyclerAdapterIT extends RecyclerView.Adapter<RecyclerAdapterIT.ViewHolder> {
    TaskList taskList;
    TaskCardViewListener listener;

    public RecyclerAdapterIT(TaskList taskList, TaskCardViewListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task t = taskList.getIncompleteTask(position);
        holder.taskTitleTV.setText(t.getTitle());
        holder.taskDetailsTV.setText(t.getDetails());
        holder.taskCheckBox.setChecked(t.isComplete());
    }

    @Override
    public int getItemCount() {
        return taskList.incompleteTaskCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitleTV;
        public TextView taskDetailsTV;
        public CheckBox taskCheckBox;
        public LinearLayout taskContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitleTV = (TextView) itemView.findViewById(R.id.task_title_tv);
            taskDetailsTV = (TextView) itemView.findViewById(R.id.task_details_tv);
            taskCheckBox = (CheckBox) itemView.findViewById(R.id.task_check_box);
            taskContainer = (LinearLayout) itemView.findViewById(R.id.task_container_LL);

            // CheckBox on click listener :
            taskCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = ViewHolder.this.getAdapterPosition();
                    listener.completeTask(position);
                }
            });

            taskContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = ViewHolder.this.getAdapterPosition();
                    listener.onTaskContainerClick(position, false);
                }
            });

        }
    }
}
