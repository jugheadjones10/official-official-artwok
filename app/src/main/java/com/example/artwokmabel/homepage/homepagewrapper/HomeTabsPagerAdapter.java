package com.example.artwokmabel.homepage.homepagewrapper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.homepage.edit.EditCategoriesFragment;
import com.example.artwokmabel.homepage.listingstab.ListingsTabFragment;
import com.example.artwokmabel.homepage.postsfeed.HomeFeedFragment;
import com.example.artwokmabel.homepage.request.tab.RequestsFragment;

import java.util.ArrayList;

public class HomeTabsPagerAdapter extends FragmentStateAdapter {
    private ArrayList<String> categoriesList;
    final private String TAG = "HomeTabsPagerAdapter";

    public HomeTabsPagerAdapter(Fragment frag) {
        super(frag);
    }

    @NonNull
    @Override public Fragment createFragment(int position) {
        String tabName = categoriesList.get(position);
        Fragment tabFragment;
        switch(tabName){
            case "feed":
                tabFragment = new HomeFeedFragment();
                break;
            case "gifts":
                tabFragment = new ListingsTabFragment();
                Bundle args = new Bundle();
                args.putString("category", "Gifts");
                tabFragment.setArguments(args);
                break;
            case "services":
                tabFragment = new ListingsTabFragment();
                Bundle args3 = new Bundle();
                args3.putString("category", "Services");
                tabFragment.setArguments(args3);
                break;
            case "handmade":
                tabFragment = new ListingsTabFragment();
                Bundle args4 = new Bundle();
                args4.putString("category", "Handmade");
                tabFragment.setArguments(args4);
                break;
            case "troll":
                tabFragment = new ListingsTabFragment();
                Bundle args5 = new Bundle();
                args5.putString("category", "Troll");
                tabFragment.setArguments(args5);
                break;
            case "flowers":
                tabFragment = new ListingsTabFragment();
                Bundle args6 = new Bundle();
                args6.putString("category", "Flowers");
                tabFragment.setArguments(args6);
                break;
            case "requests":
                tabFragment = new RequestsFragment();
                break;
            case "edit":
                tabFragment = new EditCategoriesFragment();
                break;
            default:
                tabFragment = new EditCategoriesFragment();
                break;
        }
        return tabFragment;
    }

    public void setCategoriesList(final ArrayList<String> categoriesList) {
        if (this.categoriesList == null) {
            this.categoriesList = categoriesList;

            notifyItemRangeInserted(0, categoriesList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return HomeTabsPagerAdapter.this.categoriesList.size();
                }

                @Override
                public int getNewListSize() {
                    return categoriesList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return HomeTabsPagerAdapter.this.categoriesList.get(oldItemPosition)
                            .equals(categoriesList.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return HomeTabsPagerAdapter.this.categoriesList.get(oldItemPosition)
                            .equals(categoriesList.get(newItemPosition));
                }
            });
            this.categoriesList = categoriesList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        return categoriesList == null ? 0 : categoriesList.size();
    }
}



