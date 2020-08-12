package com.example.tasks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasks.adapters.MoveTaskRecyclerAdapter;
import com.example.tasks.adapters.RecyclerAdapterCST;
import com.example.tasks.adapters.RecyclerAdapterIST;
import com.example.tasks.dataStructure.Task;
import com.example.tasks.interfaces.OnRecyclerItemClickListener;
import com.example.tasks.interfaces.OnSubTaskItemClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class TaskViewActivity extends AppCompatActivity implements OnSubTaskItemClickListener {

    private TextView listTitleTV;
    private ImageButton changeStatusButton;
    private EditText taskTitleET;
    private EditText taskDetailET;
    private TextView completeHeader;

    private RecyclerView incompleteSubTasksRecyclerView;
    RecyclerAdapterIST recyclerAdapterIST;
    private RecyclerView completeSubTaskRecyclerView;
    RecyclerAdapterCST recyclerAdapterCST;

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

        completeHeader = (TextView) findViewById(R.id.task_view_complete_sub_task_header);

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

        Button addNewSubTaskButton = (Button) findViewById(R.id.task_view_add_new_sub_task_button);
        addNewSubTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewSubTaskDialog();
            }
        });

        // recycler views
        incompleteSubTasksRecyclerView = (RecyclerView) findViewById(R.id.sub_tasks_incomplete_recycler_view);
        incompleteSubTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        completeSubTaskRecyclerView = (RecyclerView) findViewById(R.id.sub_tasks_complete_recycler_view);
        completeSubTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));


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

        recyclerAdapterIST = new RecyclerAdapterIST(task, this);
        incompleteSubTasksRecyclerView.setAdapter(recyclerAdapterIST);

        recyclerAdapterCST = new RecyclerAdapterCST(task, this);
        completeSubTaskRecyclerView.setAdapter(recyclerAdapterCST);
    }

    private void showAddNewSubTaskDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(TaskViewActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.change_sub_task_title, null);

        final EditText taskTitleET = (EditText) mView.findViewById(R.id.change_sub_task_title_ET);
        Button addNewTasButton = (Button) mView.findViewById(R.id.change_sub_task_title_button);
        addNewTasButton.setText(R.string.Add);
        alert.setView(mView);

        TextView header = (TextView) mView.findViewById(R.id.Sub_task_title_cd_header);
        header.setText(R.string.AddNewSubTask);

        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        addNewTasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitleET.getText().toString().trim();
                if (title.equals("")) {
                    makeToast("Provide a valid title");
                    alertDialog.dismiss();
                } else {
                    manager.addSubTask(title, task);
                    recyclerAdapterIST.notifyDataSetChanged();
                    alertDialog.dismiss();
                    makeToast("Sub task added");
                }
            }
        });
        alertDialog.show();
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

    @Override
    public void onCompleteButtonClick(int position, boolean complete) {
        if (complete) incompleteSubTask(position);
        else completeSubTask(position);
        // notify item removed!
    }

    private void incompleteSubTask(int position) {
        manager.incompleteSubTaskOf(task, position);
        recyclerAdapterCST.notifyItemRemoved(position);
        recyclerAdapterCST.notifyItemRangeChanged(position, recyclerAdapterCST.getItemCount());
        recyclerAdapterIST.notifyDataSetChanged();
        makeToast("One sub task marked as incomplete");
    }

    private void completeSubTask(int position) {
        manager.completeSubTaskOf(task, position);
        recyclerAdapterIST.notifyItemRemoved(position);
        recyclerAdapterIST.notifyItemRangeChanged(position, recyclerAdapterIST.getItemCount());
        recyclerAdapterCST.notifyDataSetChanged();
        makeToast("One sub task marked as complete");
    }

    @Override
    public void onDeleteButtonClick(int position , boolean complete) {
        if (complete) deleteCompleteSubTask(position);
        else deleteIncompleteSubTask(position);
        makeToast("One sub task deleted");
    }

    private void deleteCompleteSubTask(int position) {
        manager.deleteCompleteSubTask(task, position);
        recyclerAdapterCST.notifyItemRemoved(position);
        recyclerAdapterCST.notifyItemRangeChanged(position, recyclerAdapterCST.getItemCount());
    }

    private void deleteIncompleteSubTask(int position) {
        manager.deleteIncompleteSubTask(task, position);
        recyclerAdapterIST.notifyItemRemoved(position);
        recyclerAdapterIST.notifyItemRangeChanged(position, recyclerAdapterIST.getItemCount());
    }

    @Override
    public void onItemClick(int position , boolean complete)  {
        showSubTaskTitleChangeDialog(position, complete);
        // notify data changed!
    }

    private void showSubTaskTitleChangeDialog(final int position, final boolean complete) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(TaskViewActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.change_sub_task_title, null);
        final EditText taskTitleET = (EditText) mView.findViewById(R.id.change_sub_task_title_ET);
        if (complete) taskTitleET.setText(task.getCompleteSubTaskAt(position).getTitle());
        else taskTitleET.setText(task.getIncompleteSubTaskAt(position).getTitle());
        Button addNewTasButton = (Button) mView.findViewById(R.id.change_sub_task_title_button);

        TextView header = (TextView) mView.findViewById(R.id.Sub_task_title_cd_header);
        header.setText(R.string.ChangeSubTaskTitle);

        alert.setView(mView);

        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        addNewTasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitleET.getText().toString().trim();
                if (title.equals("")) {
                    makeToast("Provide a valid title");
                    alertDialog.dismiss();
                } else {
                    if (complete) manager.changeCompleteSubTaskTitle(task, position, title);
                    else manager.changeIncompleteSubTaskTitle(task, position, title);
                    if (complete) recyclerAdapterCST.notifyDataSetChanged();
                    else recyclerAdapterIST.notifyDataSetChanged();
                    alertDialog.dismiss();
                    makeToast("Title of sub task changed");
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void setCompleteHeader(boolean has) {
        if (has) completeHeader.setText(R.string.Complete);
        else completeHeader.setText("");
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