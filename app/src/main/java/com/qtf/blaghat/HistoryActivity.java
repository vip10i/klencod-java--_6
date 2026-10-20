package com.blaghat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends Activity {

    private ListView listView;
    private List<Report> reports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(16);
        root.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText("سجل البلاغات");
        title.setTextSize(22);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, dp(16));
        root.addView(title);

        listView = new ListView(this);
        root.addView(listView);
        setContentView(root);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                if (pos < reports.size()) showDetails(reports.get(pos));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                if (pos < reports.size()) confirmDelete(reports.get(pos));
                return true;
            }
        });

        reload();
    }

    private void reload() {
        DatabaseHelper db = new DatabaseHelper(this);
        reports = db.getAll();
        db.close();

        List<String> lines = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        for (Report r : reports) {
            lines.add(r.platform + " — " + r.category + "\n"
                + r.target + "\n"
                + fmt.format(new Date(r.createdAt)));
        }

        if (lines.isEmpty()) {
            lines.add("لا توجد بلاغات بعد.");
        }

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lines));
    }

    private void showDetails(Report r) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String body = "المنصة: " + r.platform + "\n\n"
            + "النوع: " + r.category + "\n\n"
            + "الهدف: " + r.target + "\n\n"
            + "التاريخ: " + fmt.format(new Date(r.createdAt)) + "\n\n"
            + "التفاصيل:\n" + (r.description == null || r.description.isEmpty() ? "(لا توجد)" : r.description);

        new AlertDialog.Builder(this)
            .setTitle("تفاصيل البلاغ")
            .setMessage(body)
            .setPositiveButton("إغلاق", null)
            .show();
    }

    private void confirmDelete(final Report r) {
        new AlertDialog.Builder(this)
            .setTitle("حذف البلاغ")
            .setMessage("هل تريد حذف هذا البلاغ؟")
            .setNegativeButton("إلغاء", null)
            .setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int w) {
                    DatabaseHelper db = new DatabaseHelper(HistoryActivity.this);
                    db.delete(r.id);
                    db.close();
                    reload();
                }
            })
            .show();
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}