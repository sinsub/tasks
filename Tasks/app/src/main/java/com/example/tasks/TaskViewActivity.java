package com.example.tasks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasks.adapters.MoveTaskRecyclerAdapter;
import com.example.tasks.dataStructure.Task;
import com.example.tasks.interfaces.OnRecyclerItemClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class TaskViewActivity extends AppCompatActivity {

    private TextView listTitleTV;
    private ImageButton changeStatusButton;
    private EditText taskTitleET;
    private EditText taskDetailET;

    private Task task;
    private Manager manager;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        Intent intent = getIntent();
        manager = new Manager(this);
        final int position = intent.getIntExtra("task_position", 0);
        boolean complete = intent.getBooleanExtra("task_status", false);
        if (complete && manager.getOpenList().completeTaskCount() <= position) {
            makeToast("Error while loading Task View due to wrong extra passed!");
            startListActivity();
        }

        if (complete)
            task = manager.getOpenList().getCompletedTask(position);
        else
            task = manager.getOpenList().getIncompleteTask(position);

        // Setting stuff up
        listTitleTV = (TextView) findViewById(R.id.list_title_tv_task_view);
        taskTitleET = (EditText) findViewById(R.id.task_title_edit_text);
        taskTitleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                manager.changeTaskTitle(task, taskTitleET.getText().toString());
            }
        });
        taskDetailET = (EditText) findViewById(R.id.task_details_edit_text);
        taskDetailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                manager.changeTaskDetails(task, taskDetailET.getText().toString());
            }
        });

        // Back button
        ImageButton backButton = (ImageButton) findViewById(R.id.back_image_button_task_view);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // delete button
        ImageButton deleteButton = (ImageButton) findViewById(R.id.delete_task_image_button_task_view);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });

        // edit list button : changes the list in which the task belongs
        ImageButton editListButton = (ImageButton) findViewById(R.id.edit_list_button_task_view);
        editListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeList();
            }
        });

        // change status button : toggles complete / incomplete
        changeStatusButton = (ImageButton) findViewById(R.id.complete_image_button_task_view);
        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus();
            }
        });

        // setting up view for the first time
        setView();
    }

    private void setView() {
        listTitleTV.setText(manager.getListTitle());
        if (task.isComplete()) {
            changeStatusButton.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24_accent);
            taskTitleET.setPaintFlags(taskTitleET.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            changeStatusButton.setImageResource(R.drawable.ic_baseline_check_24);
            taskTitleET.setPaintFlags(taskTitleET.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        taskTitleET.setText(task.getTitle());
        taskDetailET.setText(task.getDetails());
    }

    private void changeStatus() {
        if (!task.isComplete()) {
            changeStatusButton.setImageResource(R.drawable.ic_baseline_plus_one_24_respect);
        }
        manager.changeStatusOfTask(task);
        if (task.isComplete()) {
            makeToast("Marked task as complete");
        } else {
            makeToast("Marked task as incomplete");
        }
        setView();
    }

    private void changeList() {
        View dialogView = getLayoutInflater().inflate(R.layout.move_task_to_list_bottom_sheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        OnRecyclerItemClickListener listener = new OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerItemCLick(int position) {
                manager.moveTaskToList(task, position);
                startListActivity();
                dialog.cancel();
                finish();
            }
        };
        RecyclerView moveTaskRecyclerView = (RecyclerView) dialogView.findViewById(R.id.move_task_to_recycler_view);
        moveTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MoveTaskRecyclerAdapter moveTaskRecyclerAdapter = new MoveTaskRecyclerAdapter(manager.getUser(), listener, this);
        moveTaskRecyclerView.setAdapter(moveTaskRecyclerAdapter);
        dialog.show();
    }

    private void deleteTask() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(TaskViewActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.continue_or_cancle, null);

        final TextView messageTextView = (TextView) mView.findViewById(R.id.message_text_view);
        Button positiveButton = (Button) mView.findViewById(R.id.positive_button);
        Button negativeButton = (Button) mView.findViewById(R.id.negative_button);

        alert.setView(mView);
        String message = "The task will be deleted forever.";
        messageTextView.setText(message);

        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        positiveButton.setText(R.string.Delete);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.deleteTask(task);
                makeToast("Task deleted");
                alertDialog.cancel();
                startListActivity();
            }
        });

        negativeButton.setText(R.string.Cancel);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void startListActivity() {
        onBackPressed();
        finish();
    }

    public void makeToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}