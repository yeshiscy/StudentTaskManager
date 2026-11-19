package com.example.studenttaskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.security.MessageDigest;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentTasks.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_USERS = "users";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TASKS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, description TEXT, due_date TEXT, " +
                "completed INTEGER DEFAULT 0)");
        createUsersTable(db);
    }

    private void createUsersTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) createUsersTable(db);
    }

    // ---------- USERS ----------
    private String hash(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(s.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte x : b) sb.append(String.format("%02x", x));
            return sb.toString();
        } catch (Exception e) {
            return s;
        }
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("username", username);
        v.put("password", hash(password));
        return db.insert(TABLE_USERS, null, v) != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM " + TABLE_USERS +
                        " WHERE username=? AND password=?",
                new String[]{username, hash(password)});
        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }

    // ---------- TASKS ----------
    public boolean addTask(String title, String description, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("due_date", dueDate);
        values.put("completed", 0);
        return db.insert(TABLE_TASKS, null, values) != -1;
    }

    // filter: -1 all, 0 pending, 1 completed, 2 overdue
    public ArrayList<Task> getTasks(int filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = "";
        if (filter == 0) where = " WHERE completed=0";
        else if (filter == 1) where = " WHERE completed=1";
        else if (filter == 2)
            where = " WHERE completed=0 AND due_date < date('now','localtime')";

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TASKS + where +
                " ORDER BY due_date ASC", null);
        ArrayList<Task> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(new Task(c.getInt(0), c.getString(1), c.getString(2),
                    c.getString(3), c.getInt(4) == 1));
        }
        c.close();
        return list;
    }

    public int getCount(int status) {   // -1 all, 0 pending, 1 completed
        return getTasks(status).size();
    }

    public int getOverdueCount() {
        return getTasks(2).size();
    }

    public boolean updateTask(int id, String title, String description, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("due_date", dueDate);
        return db.update(TABLE_TASKS, values, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TASKS, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("completed", status);
        return db.update(TABLE_TASKS, values, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }
}