package com.example.pethousekeeper;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    public AboutFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 加載 fragment_about.xml 佈局文件
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView aboutTextView = view.findViewById(R.id.about_text_view);

        // 定義應用背景介紹和電子郵件地址
        String appBackground = "Our purpose in developing this app is to enable all animal lovers to better understand their pets' health conditions and to help record their pets' growth processes. To achieve this, our app offers a comprehensive suite of features designed to support pet owners in every aspect of their pets' lives. ";
        String emailText = "Context Us: pethousekeeper2024@gmail.com";

        // 創建 SpannableString
        SpannableString spannableString = new SpannableString(appBackground + "\n" + emailText);

        // 找到電子郵件地址的位置
        int start = spannableString.toString().indexOf("pethousekeeper2024@gmail.com");
        int end = start + "pethousekeeper2024@gmail.com".length();

        if (start != -1) {
            // 創建 ClickableSpan
            ClickableSpan emailClickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:pethousekeeper2024@gmail.com"));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Support");

                    if (emailIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        startActivity(emailIntent);
                    } else {
                        Toast.makeText(getContext(), "We can't find this email.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(true); // 下劃線
                    ds.setColor(Color.BLUE);     // 藍色
                }
            };

            // 應用 ClickableSpan
            spannableString.setSpan(emailClickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 設置 TextView
        aboutTextView.setText(spannableString);
        aboutTextView.setMovementMethod(LinkMovementMethod.getInstance());
        aboutTextView.setHighlightColor(Color.TRANSPARENT); // 可選
    }
}