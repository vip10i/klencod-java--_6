package com.blaghat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReportActivity extends Activity {

    private String platform;
    private Spinner categorySpinner;
    private EditText targetInput;
    private EditText descInput;

    private static final String[] CATEGORIES = {
        "اختر نوع المخالفة",
        "احتيال أو نصب",
        "محتوى مسيء أو تحريضي",
        "انتحال شخصية",
        "انتهاك الخصوصية",
        "محتوى مخالف للقوانين",
        "بريد مزعج / سبام",
        "حساب مزيف",
        "أخرى"
    };

    private static String officialReportUrl(String p) {
        switch (p) {
            case "واتساب":  return "https://faq.whatsapp.com/1142481766359885";
            case "تيك توك": return "https://www.tiktok.com/legal/report/feedback";
            case "إنستغرام": return "https://help.instagram.com/contact/383679321740945";
            case "فيسبوك":  return "https://www.facebook.com/help/contact/243534049026609";
            default: return "https://www.google.com";
        }
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        platform = getIntent().getStringExtra("platform");
        if (platform == null) platform = "منصة";

        ScrollView sv = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(20);
        root.setPadding(pad, pad, pad, pad);
        sv.addView(root);

        TextView header = new TextView(this);
        header.setText("بلاغ ضد: " + platform);
        header.setTextSize(22);
        header.setGravity(Gravity.CENTER);
        header.setPadding(0, 0, 0, dp(20));
        root.addView(header);

        TextView lbl1 = new TextView(this);
        lbl1.setText("الحساب أو الرابط المستهدف");
        lbl1.setPadding(0, 0, 0, dp(6));
        root.addView(lbl1);

        targetInput = new EditText(this);
        targetInput.setHint("مثال: @username أو رابط المنشور");
        targetInput.setSingleLine(true);
        root.addView(targetInput);

        TextView lbl2 = new TextView(this);
        lbl2.setText("نوع المخالفة");
        lbl2.setPadding(0, dp(16), 0, dp(6));
        root.addView(lbl2);

        categorySpinner = new Spinner(this);
        categorySpinner.setAdapter(new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_dropdown_item, CATEGORIES));
        root.addView(categorySpinner);

        TextView lbl3 = new TextView(this);
        lbl3.setText("تفاصيل البلاغ");
        lbl3.setPadding(0, dp(16), 0, dp(6));
        root.addView(lbl3);

        descInput = new EditText(this);
        descInput.setHint("اشرح ما حدث بالتفصيل...");
        descInput.setMinLines(4);
        descInput.setGravity(Gravity.TOP | Gravity.START);
        root.addView(descInput);

        Button save = new Button(this);
        save.setText("حفظ البلاغ");
        save.setTextSize(16);
        save.setAllCaps(false);
        LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(56));
        slp.setMargins(0, dp(24), 0, dp(12));
        save.setLayoutParams(slp);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { saveReport(); }
        });
        root.addView(save);

        Button open = new Button(this);
        open.setText("فتح صفحة الإبلاغ الرسمية لـ " + platform);
        open.setTextSize(14);
        open.setAllCaps(false);
        open.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(56)));
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(officialReportUrl(platform))));
                } catch (Exception e) {
                    Toast.makeText(ReportActivity.this, "لا يوجد متصفح", Toast.LENGTH_SHORT).show();
                }
            }
        });
        root.addView(open);

        setContentView(sv);
    }

    private void saveReport() {
        String target = targetInput.getText().toString().trim();
        String cat = CATEGORIES[categorySpinner.getSelectedItemPosition()];
        String desc = descInput.getText().toString().trim();

        if (categorySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "اختر نوع المخالفة", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(target)) {
            Toast.makeText(this, "اكتب الحساب أو الرابط المستهدف", Toast.LENGTH_SHORT).show();
            return;
        }

        Report r = new Report();
        r.platform = platform;
        r.category = cat;
        r.target = target;
        r.description = desc;
        r.createdAt = System.currentTimeMillis();

        DatabaseHelper db = new DatabaseHelper(this);
        db.insert(r);
        db.close();

        Toast.makeText(this, "تم حفظ البلاغ", Toast.LENGTH_SHORT).show();
        finish();
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}