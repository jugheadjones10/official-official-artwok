package com.example.artwokmabel.RubbishReference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artwokmabel.R;

public class SearchDefaultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchdefault);

        LinearLayout gallery = findViewById(R.id.gallery);

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < 6; i++) {
            View view = inflater .inflate(R.layout.activity_searchcategories, gallery, false);

            TextView textView = view.findViewById(R.id.tv_catname);
            textView.setText("Item" + i);

            ImageView imageView = view.findViewById(R.id.iv_catpic);
            imageView.setImageResource(R.drawable.cat_cake);

            gallery.addView(view);
        }
    }
}
