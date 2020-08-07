package com.example.tasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.IResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tasks.adapters.RecyclerAdapterTL;
import com.example.tasks.interfaces.OnListItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ShowListsActivity extends AppCompatActivity implements OnListItemClickListener {

    private Manager manager;

    private RecyclerView recyclerViewTL;
    private RecyclerAdapterTL recyclerAdapterTL;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lists);

        // setting up stuff:
        manager = new Manager(this);
        recyclerViewTL = (RecyclerView) findViewById(R.id.task_lists_recycler_view);
        recyclerViewTL.setLayoutManager(new LinearLayoutManager(this));

        // ItemTouchHelper for recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewTL);

        // floating button
        FloatingActionButton addNewListButton = (FloatingActionButton) findViewById(R.id.add_task_list_fab);
        addNewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewList();
            }
        });

        // Back image button
        ImageButton backImageButton = (ImageButton) findViewById(R.id.back_image_button);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setThisView();
    }

    private void setThisView() {
        recyclerAdapterTL = new RecyclerAdapterTL(manager.getUser(), this);
        recyclerViewTL.setAdapter(recyclerAdapterTL);
    }

    private void addNewList() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ShowListsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.new_task_list_cd, null);

        final EditText taskListTitleET = (EditText) mView.findViewById(R.id.new_task_list_title_ET);
        Button addNewListButton = (Button) mView.findViewById(R.id.add_new_list_button);

        alert.setView(mView);

        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        addNewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskListTitleET.getText().toString().trim();
                if (title.equals("")) {
                    Toast.makeText(ShowListsActivity.this, "Provide a valid List name", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else {
                    manager.addNewList(title);
                    alertDialog.dismiss();
                    Toast.makeText(ShowListsActivity.this, "New List added!", Toast.LENGTH_SHORT).show();

                    recyclerAdapterTL.notifyDataSetChanged();
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void openListAtIndex(int index) {
        manager.setOpenIndex(index);
        startListActivity();
    }

    private void startListActivity() {
        Intent myIntent = new Intent(this, ListViewActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void makeToast(String s) {
        Toast.makeText(this, "Clicky Clicky neko Ureshi! "+s , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startListActivity();
        finish();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback((ItemTouchHelper.UP | ItemTouchHelper.DOWN), 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (fromPosition != 0 && toPosition != 0) {
                manager.swapTaskLists(fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            }
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(ShowListsActivity.this, R.color.colorSecondaryBg));
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(ShowListsActivity.this, R.color.colorPrimaryBg));
        }
    };
}