package com.example.artwokmabel.homepage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.adapters.CategoriesAdapter;
import com.example.artwokmabel.homepage.models.Categories;
import com.example.artwokmabel.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    List<Categories> categoriesList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, null);

        categoriesList = new ArrayList<>();
        categoriesList.add(new Categories("Gifts for Her",R.drawable.handmade_jewelry));
        categoriesList.add(new Categories("Gifts for Him",R.drawable.tiger));
        categoriesList.add(new Categories("Desserts and Cakes",R.drawable.chocolate_cake));
        categoriesList.add(new Categories("Bags and Purses",R.drawable.bags));
        categoriesList.add(new Categories("Paintings and Drawings",R.drawable.paintings));
        categoriesList.add(new Categories("Toys",R.drawable.toys));

        RecyclerView recyclerView = root.findViewById(R.id.categories_recyclerview);
        CategoriesAdapter adapter = new CategoriesAdapter(getContext(), categoriesList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);

        return root;
    }

//    public void setSingleEvent(GridLayout maingrid) {
//        for (int i = 0; i < maingrid.getChildCount(); i++) {
//            final CardView cardView = (CardView) maingrid.getChildAt(i);
//            final int finalI = i;
//
//            cardView.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View view, MotionEvent event) {
//                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                        Toast.makeText(getContext(), "Button: " + finalI, Toast.LENGTH_SHORT).show();
//                        cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.artwok_lightblue));
//                        if (finalI == 0) {
//                            getContext().startActivity(new Intent(getContext(), SettingsActivity.class));
//                        }
//                    } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
//                        /* Reset Color */
//                        cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.artwok_orange));
//                    }
//                    return true;
//                }
//            });
//        }
//    }
}
