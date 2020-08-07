package com.example.tasks.adapters;


import android.graphics.Paint;
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

public class RecyclerAdapterCT extends RecyclerView.Adapter<RecyclerAdapterCT.ViewHolder> {

    TaskList taskList;
    TaskCardViewListener listener;

    public RecyclerAdapterCT(TaskList taskList, TaskCardViewListener listener) {
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
        final Task t = taskList.getCompletedTask(position);
        holder.taskTitleTV.setText(t.getTitle());
        holder.taskDetailsTV.setText(t.getDetails());
        holder.taskCheckBox.setChecked(t.isComplete());

        holder.taskTitleTV.setPaintFlags(holder.taskTitleTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        int count = taskList.completeTaskCount();
        if (count == 0) {
            listener.setCompletedTaskListTitle(null);
        } else {
            listener.setCompletedTaskListTitle("Completed");
        }

        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView taskTitleTV;
        public TextView taskDetailsTV;
        public CheckBox taskCheckBox;
        public CardView taskHolderCV;
        public LinearLayout taskContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitleTV = (TextView) itemView.findViewById(R.id.task_title_tv);
            taskDetailsTV = (TextView) itemView.findViewById(R.id.task_details_tv);
            taskCheckBox = (CheckBox) itemView.findViewById(R.id.task_check_box);
            taskContainer = (LinearLayout) itemView.findViewById(R.id.task_container_LL);

            taskCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = ViewHolder.this.getAdapterPosition();
                    listener.incompleteTask(position);
                }
            });

            taskContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = ViewHolder.this.getAdapterPosition();
                    listener.onTaskContainerClick(position, true);
                }
            });
        }
    }
}
