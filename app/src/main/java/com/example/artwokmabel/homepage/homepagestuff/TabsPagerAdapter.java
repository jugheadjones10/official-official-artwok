package com.example.artwokmabel.homepage.homepagestuff;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.homepage.fragments.EditCategoriesFragment;
import com.example.artwokmabel.homepage.fragments.HomeFeedFragment;
import com.example.artwokmabel.homepage.fragments.HomeStandardCatFragment;

import java.util.ArrayList;

public class TabsPagerAdapter extends FragmentStateAdapter {
    private ArrayList<String> categoriesList;
    final private String TAG = "TabsPagerAdapter";

    public TabsPagerAdapter(Fragment frag) {
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
                tabFragment = new HomeStandardCatFragment();
                Bundle args = new Bundle();
                args.putString("category", "Gifts");
                tabFragment.setArguments(args);
                break;
            case "requests":
                tabFragment = new HomeStandardCatFragment();
                Bundle args2 = new Bundle();
                args2.putString("category", "Requests");
                tabFragment.setArguments(args2);
                break;
            case "services":
                tabFragment = new HomeStandardCatFragment();
                Bundle args3 = new Bundle();
                args3.putString("category", "Services");
                tabFragment.setArguments(args3);
                break;
            case "handmade":
                tabFragment = new HomeStandardCatFragment();
                Bundle args4 = new Bundle();
                args4.putString("category", "Handmade");
                tabFragment.setArguments(args4);
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
                    return TabsPagerAdapter.this.categoriesList.size();
                }

                @Override
                public int getNewListSize() {
                    return categoriesList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return TabsPagerAdapter.this.categoriesList.get(oldItemPosition)
                            .equals(categoriesList.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return TabsPagerAdapter.this.categoriesList.get(oldItemPosition)
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



