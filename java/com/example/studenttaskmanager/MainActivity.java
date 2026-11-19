package com.example.studenttaskmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity {

    EditText edtTitle, edtDescription, edtDueDate;
    Button btnAdd;
    ListView listTasks;
    Spinner spnFilter;
    TextView txtEmpty;
    DatabaseHelper databaseHelper;
    ArrayList<Task> tasks = new ArrayList<>();
    TaskAdapter adapter;
    int currentFilter = -1;   // -1 all, 0 pending, 1 completed, 2 overdue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        listTasks = findViewById(R.id.listTasks);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // The form lives at the top of the list
        View header = getLayoutInflater()
                .inflate(R.layout.header_task_form, listTasks, false);
        listTasks.addHeaderView(header, null, false);

        edtTitle = header.findViewById(R.id.edtTitle);
        edtDescription = header.findViewById(R.id.edtDescription);
        edtDueDate = header.findViewById(R.id.edtDueDate);
        btnAdd = header.findViewById(R.id.btnAdd);
        spnFilter = header.findViewById(R.id.spnFilter);
        txtEmpty = header.findViewById(R.id.txtEmpty);

        adapter = new TaskAdapter(this, tasks);
        listTasks.setAdapter(adapter);

        edtDueDate.setFocusable(false);
        edtDueDate.setOnClickListener(v -> pickDate(edtDueDate));

        String[] filters = {"All Tasks", "Pending", "Completed", "Overdue"};
        spnFilter.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, filters));
        spnFilter.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                        currentFilter = (pos == 0) ? -1 : pos - 1;
                        displayTasks();
                    }
                    @Override public void onNothingSelected(AdapterView<?> p) {}
                });

        btnAdd.setOnClickListener(v -> addTask());

        // Tap a task: toggle Pending / Completed
        listTasks.setOnItemClickListener((parent, view, position, id) -> {
            Task t = (Task) parent.getItemAtPosition(position);
            if (t == null) return;
            databaseHelper.updateStatus(t.id, t.completed ? 0 : 1);
            displayTasks();
            Toast.makeText(this, "Task status updated!",
                    Toast.LENGTH_SHORT).show();
        });

        // Long-press a task: Edit / Delete
        listTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            Task t = (Task) parent.getItemAtPosition(position);
            if (t == null) return true;
            new AlertDialog.Builder(this)
                    .setTitle("Manage Task")
                    .setItems(new String[]{"Edit Task", "Delete Task"},
                            (d, which) -> {
                                if (which == 0) editTask(t);
                                else confirmDelete(t.id);
                            })
                    .show();
            return true;
        });

        displayTasks();
    }

    private void pickDate(EditText target) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, y, m, d) ->
                target.setText(String.format(Locale.US, "%04d-%02d-%02d",
                        y, m + 1, d)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void addTask() {
        String title = edtTitle.getText().toString().trim();
        String desc = edtDescription.getText().toString().trim();
        String due = edtDueDate.getText().toString().trim();

        if (title.isEmpty() || desc.isEmpty() || due.isEmpty()) {
            Toast.makeText(this, "Please complete all fields!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (databaseHelper.addTask(title, desc, due)) {
            Toast.makeText(this, "Task added successfully!",
                    Toast.LENGTH_SHORT).show();
            edtTitle.setText("");
            edtDescription.setText("");
            edtDueDate.setText("");
            displayTasks();
        } else {
            Toast.makeText(this, "Unable to save task!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void displayTasks() {
        tasks.clear();
        tasks.addAll(databaseHelper.getTasks(currentFilter));
        adapter.notifyDataSetChanged();
        txtEmpty.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void editTask(Task t) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_task, null);
        EditText dlgTitle = dialogView.findViewById(R.id.dlgTitle);
        EditText dlgDesc = dialogView.findViewById(R.id.dlgDesc);
        EditText dlgDue = dialogView.findViewById(R.id.dlgDue);

        dlgTitle.setText(t.title);
        dlgDesc.setText(t.description);
        dlgDue.setText(t.dueDate);
        dlgDue.setFocusable(false);
        dlgDue.setOnClickListener(v -> pickDate(dlgDue));

        new AlertDialog.Builder(this)
                .setTitle("Edit Task")
                .setView(dialogView)
                .setPositiveButton("UPDATE", (d, w) -> {
                    String nt = dlgTitle.getText().toString().trim();
                    String nd = dlgDesc.getText().toString().trim();
                    String ndue = dlgDue.getText().toString().trim();
                    if (nt.isEmpty() || nd.isEmpty() || ndue.isEmpty()) {
                        Toast.makeText(this, "All fields are required!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    databaseHelper.updateTask(t.id, nt, nd, ndue);
                    displayTasks();
                    Toast.makeText(this, "Task updated!",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void confirmDelete(int taskId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("YES", (d, w) -> {
                    databaseHelper.deleteTask(taskId);
                    displayTasks();
                    Toast.makeText(this, "Task deleted!",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("NO", null)
                .show();
    }
}