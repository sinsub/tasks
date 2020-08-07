package com.example.tasks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasks.R;
import com.example.tasks.dataStructure.User;
import com.example.tasks.interfaces.OnListItemClickListener;

public class RecyclerAdapterTL extends RecyclerView.Adapter<RecyclerAdapterTL.ViewHolder> {

    User user;
    OnListItemClickListener listener;

    public RecyclerAdapterTL(User user, OnListItemClickListener listener) {
        this.user = user;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = user.getTaskListTitle(position);
        holder.listNameTV.setText(s);
        String number = Integer.toString(user.numberOfIncompleteTasks(position));
        holder.listDueTasks.setText(number);
    }

    @Override
    public int getItemCount() {
        return user.getTaskListCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView listNameTV;
        public TextView listDueTasks;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listNameTV = (TextView) itemView.findViewById(R.id.task_list_title_tv);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.task_list_item_container);
            listDueTasks = (TextView) itemView.findViewById(R.id.task_list_tasks_due);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.openListAtIndex(position);
                }
            });
        }
    }
}
