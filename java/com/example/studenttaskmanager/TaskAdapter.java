package com.example.studenttaskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Paint;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_task, parent, false);
        }

        Task t = getItem(position);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                .format(new Date());

        View bar = convertView.findViewById(R.id.viewBar);
        TextView title = convertView.findViewById(R.id.txtTaskTitle);
        TextView desc = convertView.findViewById(R.id.txtTaskDesc);
        TextView due = convertView.findViewById(R.id.txtTaskDue);
        TextView status = convertView.findViewById(R.id.txtStatus);

        title.setText(t.title);
        desc.setText(t.description);
        due.setText("Due: " + t.dueDate);

        if (t.completed) {
            status.setText("COMPLETED");
            status.setTextColor(0xFF1E7F4F);
            status.setBackgroundResource(R.drawable.chip_completed);
            bar.setBackgroundColor(0xFF22A06B);
            title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            title.setTextColor(0xFF8A94A6);
        } else if (t.dueDate.compareTo(today) < 0) {
            status.setText("OVERDUE");
            status.setTextColor(0xFFB42318);
            status.setBackgroundResource(R.drawable.chip_overdue);
            bar.setBackgroundColor(0xFFE5484D);
            title.setPaintFlags(title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            title.setTextColor(0xFF1B2A41);
        } else {
            status.setText("PENDING");
            status.setTextColor(0xFFB7791F);
            status.setBackgroundResource(R.drawable.chip_pending);
            bar.setBackgroundColor(0xFFF59E0B);
            title.setPaintFlags(title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            title.setTextColor(0xFF1B2A41);
        }
        return convertView;
    }
}