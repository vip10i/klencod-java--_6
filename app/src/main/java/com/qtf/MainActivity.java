package com.blaghat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String[] PLATFORMS = {"واتساب", "تيك توك", "إنستغرام", "فيسبوك"};
    private static final int[] COLORS = {
        Color.parseColor("#25D366"),
        Color.parseColor("#000000"),
        Color.parseColor("#E1306C"),
        Color.parseColor("#1877F2")
    };

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        ScrollView sv = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(20);
        root.setPadding(pad, pad, pad, pad);
        sv.addView(root);

        TextView title = new TextView(this);
        title.setText("تطبيق البلاغات");
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, dp(4));
        root.addView(title);

        TextView sub = new TextView(this);
        sub.setText("اختر المنصة لتقديم بلاغ");
        sub.setTextSize(14);
        sub.setGravity(Gravity.CENTER);
        sub.setPadding(0, 0, 0, dp(24));
        root.addView(sub);

        for (int i = 0; i < PLATFORMS.length; i++) {
            final String platform = PLATFORMS[i];
            Button btn = new Button(this);
            btn.setText(platform);
            btn.setTextSize(18);
            btn.setAllCaps(false);
            btn.setTextColor(Color.WHITE);
            btn.setBackgroundColor(COLORS[i]);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(64));
            lp.setMargins(0, 0, 0, dp(12));
            btn.setLayoutParams(lp);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, ReportActivity.class);
                    i.putExtra("platform", platform);
                    startActivity(i);
                }
            });
            root.addView(btn);
        }

        Button history = new Button(this);
        history.setText("سجل البلاغات");
        history.setTextSize(16);
        history.setAllCaps(false);
        LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(56));
        hlp.setMargins(0, dp(16), 0, 0);
        history.setLayoutParams(hlp);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
        root.addView(history);

        setContentView(sv);
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}