package com.example.tasks.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasks.Manager;
import com.example.tasks.R;
import com.example.tasks.dataStructure.SubTask;
import com.example.tasks.dataStructure.Task;
import com.example.tasks.dataStructure.TaskList;
import com.example.tasks.interfaces.OnNestedSubTaskItemListener;
import com.example.tasks.interfaces.TaskCardViewListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class RecyclerAdapterIT extends RecyclerView.Adapter<RecyclerAdapterIT.ViewHolder> {
    TaskList taskList;
    TaskCardViewListener listener;
    Context context;
    Manager manager;
    FloatingActionButton addTaskFAB;

    public RecyclerAdapterIT(TaskList taskList, TaskCardViewListener listener, Context context, Manager manager,FloatingActionButton addTaskFAB ) {
        this.taskList = taskList;
        this.listener = listener;
        this.context = context;
        this.manager = manager;
        this.addTaskFAB = addTaskFAB;       // To anchor the SnackBar
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
        String details = t.getDetails();
        if (details == null || details.equals(""))
            holder.taskDetailsTV.setVisibility(View.GONE);
        else
            holder.taskDetailsTV.setText(details);
        // recycler view
        holder.recyclerAdapterNIST = new RecyclerAdapterNIST(taskList.getIncompleteTask(position), holder.onNestedSubTaskItemListener);
        holder.subTasksRV.setAdapter(holder.recyclerAdapterNIST);
    }

    @Override
    public int getItemCount() {
        return taskList.incompleteTaskCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitleTV;
        public TextView taskDetailsTV;
        public ImageButton taskCompleteButton;
        public LinearLayout taskContainer;
        public RecyclerView subTasksRV;
        public RecyclerAdapterNIST recyclerAdapterNIST;
        public OnNestedSubTaskItemListener onNestedSubTaskItemListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitleTV = (TextView) itemView.findViewById(R.id.task_title_tv);
            taskDetailsTV = (TextView) itemView.findViewById(R.id.task_details_tv);
            taskCompleteButton = (ImageButton) itemView.findViewById(R.id.task_item_complete_button);
            taskContainer = (LinearLayout) itemView.findViewById(R.id.task_item_container);
            subTasksRV = (RecyclerView) itemView.findViewById(R.id.task_card_view_sub_task_recycler_view);
            subTasksRV.setLayoutManager(new LinearLayoutManager(context));

            // CheckBox on click listener :
            taskCompleteButton.setOnClickListener(new View.OnClickListener() {
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

            onNestedSubTaskItemListener = new OnNestedSubTaskItemListener() {

                @Override
                public void onSubTaskCompleteButtonClick(final int subTaskPosition, boolean complete) {
                    final SubTask subTask = manager.completeSubTaskOf(manager.getOpenList().getIncompleteTask(getAdapterPosition()), subTaskPosition);
                    recyclerAdapterNIST.notifyItemRemoved(subTaskPosition);
                    Snackbar snackbar = Snackbar.make(subTasksRV, R.string.SubTaskMarkedAsComplete, Snackbar.LENGTH_LONG);
                    snackbar.setAnchorView(addTaskFAB);
                    snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorAccentSecondary));
                    snackbar.setAction(R.string.Undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            manager.undoCompleteSubTaskOf(manager.getOpenList().getIncompleteTask(getAdapterPosition()), subTask, subTaskPosition);
                            recyclerAdapterNIST.notifyItemInserted(subTaskPosition);
                        }
                    });
                    snackbar.show();
                }

                @Override
                public void onSubTaskDeleteButtonClick(final int subTaskPosition, boolean complete) {
                    final SubTask subTask = taskList.getIncompleteTask(getAdapterPosition()).getIncompleteSubTaskAt(subTaskPosition);
                    manager.deleteIncompleteSubTask(manager.getOpenList().getIncompleteTask(getAdapterPosition()), subTaskPosition);
                    recyclerAdapterNIST.notifyItemRemoved(subTaskPosition);

                    Snackbar snackbar = Snackbar.make(subTasksRV, R.string.SubTaskDeleted, Snackbar.LENGTH_LONG);
                    snackbar.setAnchorView(addTaskFAB);
                    snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorAccentSecondary));
                    snackbar.setAction(R.string.Undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            manager.undoDeleteIncompleteSubTask(taskList.getIncompleteTask(getAdapterPosition()), subTask, subTaskPosition);
                            recyclerAdapterNIST.notifyItemInserted(subTaskPosition);
                        }
                    });
                    snackbar.show();
                }


                @Override
                public void onSubTaskItemClick(int subTaskPosition, boolean complete) {
                    listener.onSubTaskItemClick(getAdapterPosition(), subTaskPosition, complete);
                }
            };

        }
    }
}
