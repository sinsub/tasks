package com.example.tasks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasks.R;
import com.example.tasks.dataStructure.User;
import com.example.tasks.interfaces.OnRecyclerItemClickListener;

public class MoveTaskRecyclerAdapter extends RecyclerView.Adapter<MoveTaskRecyclerAdapter.ViewHolder> {

    User user;
    OnRecyclerItemClickListener listener;
    Context context;

    public MoveTaskRecyclerAdapter(User user, OnRecyclerItemClickListener listener, Context context){
        this.user = user;
        this.listener = listener;
        this.context = context;
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
        if (position == user.getOpenIndex()) {
            holder.listNameTV.setTextColor(ContextCompat.getColor(context, R.color.colorAccentPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return user.getTaskListCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView listNameTV;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listNameTV = (TextView) itemView.findViewById(R.id.task_list_title_tv);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.task_list_item_container);
            linearLayout.setOnClickListener(v -> {
                int position = getAdapterPosition();
                listener.onRecyclerItemCLick(position);
            });
        }
    }
}
