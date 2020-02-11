package com.example.artwokmabel.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.chat.tabs.MessageChatsFragment;
import com.example.artwokmabel.chat.tabs.MessageFollowingFragment;
import com.example.artwokmabel.chat.tabs.MessageOrdersFragment;
import com.example.artwokmabel.homepage.edit.EditCategoriesFragment;
import com.example.artwokmabel.homepage.homepagewrapper.HomeTabsPagerAdapter;
import com.example.artwokmabel.homepage.listingstab.ListingsTabFragment;
import com.example.artwokmabel.homepage.postsfeed.HomeFeedFragment;
import com.example.artwokmabel.homepage.request.tab.RequestsFragment;

import java.util.ArrayList;
import java.util.List;

public class MessageFragmentPagerAdapter extends FragmentStateAdapter {

    public MessageFragmentPagerAdapter(Fragment frag) {
        super(frag);
    }

    @NonNull
    @Override public Fragment createFragment(int position) {
        Fragment tabFragment;
        switch(position){
            case 0:
                tabFragment = new MessageFollowingFragment();
                break;
            case 1:
                tabFragment = new MessageChatsFragment();
                break;
            case 2:
                tabFragment = new MessageOrdersFragment();
                break;
            default:
                tabFragment = new MessageOrdersFragment();
                break;
        }
        return tabFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

//        extends FragmentPagerAdapter {
//
//    private final List<Fragment> mFragmentList = new ArrayList<>();
//    private final List<String> mFragmentTitleList = new ArrayList<>();
//
//    public void addFragment(Fragment fragment, String title){
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(title);
//    }
//
//    public MessageFragmentPagerAdapter(FragmentManager fm){
//        super(fm);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return mFragmentList.get(position);
//    }
//
//    public CharSequence getPageTitle(int position) {
//        return mFragmentTitleList.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return mFragmentList.size() ;
//    }
//
//}