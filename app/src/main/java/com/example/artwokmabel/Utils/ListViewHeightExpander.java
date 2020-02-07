//package com.example.artwokmabel.Utils;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Adapter;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//public class ListViewHeightExpander {
//    public static void setListViewHeightBasedOnChildren(RecyclerView listView) {
//        RecyclerView.Adapter listAdapter = listView.getAdapter();
//
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getItemCount(); i++) {
//            View listItem = listAdapter.get(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getItemCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }
//
//}
