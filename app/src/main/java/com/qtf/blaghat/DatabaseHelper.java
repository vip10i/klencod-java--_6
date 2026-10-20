package com.blaghat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "reports.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "reports";

    public DatabaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "platform TEXT NOT NULL, " +
            "category TEXT, " +
            "target TEXT, " +
            "description TEXT, " +
            "created_at INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public long insert(Report r) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("platform", r.platform);
        cv.put("category", r.category);
        cv.put("target", r.target);
        cv.put("description", r.description);
        cv.put("created_at", r.createdAt);
        long id = db.insert(TABLE, null, cv);
        db.close();
        return id;
    }

    public List<Report> getAll() {
        List<Report> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE, null, null, null, null, null, "created_at DESC");
        while (c.moveToNext()) {
            Report r = new Report();
            r.id = c.getLong(c.getColumnIndexOrThrow("id"));
            r.platform = c.getString(c.getColumnIndexOrThrow("platform"));
            r.category = c.getString(c.getColumnIndexOrThrow("category"));
            r.target = c.getString(c.getColumnIndexOrThrow("target"));
            r.description = c.getString(c.getColumnIndexOrThrow("description"));
            r.createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
            out.add(r);
        }
        c.close();
        db.close();
        return out;
    }

    public void delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}