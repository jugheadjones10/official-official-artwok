package com.example.artwokmabel.chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artwokmabel.chat.adapters.OrderAdapter;
import com.example.artwokmabel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatOrderActivity extends AppCompatActivity {

    ImageButton reportbtn;

    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    OrderAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_order);

        reportbtn = findViewById(R.id.reportbtn);

        reportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatOrderActivity.this, ChatOrderReviewActivity.class);
                startActivity(i);
            }
        });

        expandableListView = findViewById(R.id.chat_order_explistview);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new OrderAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);
        initListData();
    }

    private void initListData() {
        listGroup.add(getString(R.string.orderdelivery));
        listGroup.add(getString(R.string.orderrefund));

        String[] array;

        List<String> delivery = new ArrayList<>();
        array = getResources().getStringArray(R.array.orderdelivery);
        for (String item : array) {
            delivery.add(item);
        }

        List<String> refund = new ArrayList<>();
        array = getResources().getStringArray(R.array.orderrefund);
        for (String item : array) {
            refund.add(item);
        }

        listItem.put(listGroup.get(0), delivery);
        listItem.put(listGroup.get(1), refund);
        adapter.notifyDataSetChanged();
    }
}
