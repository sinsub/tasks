package com.example.tasks.adapters;


import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tasks.ListViewActivity;
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
        holder.taskDetailsTV.setVisibility(View.GONE);
        holder.taskDueDateTimeTV.setVisibility(View.GONE);
        holder.taskIncompleteButton.setImageResource(R.drawable.ic_baseline_check_24);
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
        public TextView taskDueDateTimeTV;
        public ImageButton taskIncompleteButton;
        public CardView taskHolderCV;
        public LinearLayout taskContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitleTV = (TextView) itemView.findViewById(R.id.task_title_tv);
            taskDetailsTV = (TextView) itemView.findViewById(R.id.task_details_tv);
            taskDueDateTimeTV = (TextView) itemView.findViewById(R.id.task_due_date_time_tv);
            taskIncompleteButton = (ImageButton) itemView.findViewById(R.id.task_item_complete_button);
            taskContainer = (LinearLayout) itemView.findViewById(R.id.task_item_container);

            taskIncompleteButton.setOnClickListener(v -> {
                int position = ViewHolder.this.getAdapterPosition();
                listener.incompleteTask(position);
            });

            taskContainer.setOnClickListener(v -> {
                int position = ViewHolder.this.getAdapterPosition();
                listener.onTaskContainerClick(position, true);
            });
        }
    }
}
