package com.example.artwokmabel.homepage.listingstab;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.artwokmabel.R;
import com.example.artwokmabel.homepage.homepagewrapper.HomeTabsFragment;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

public class ShrinkListingsAnimation implements ViewPager2.PageTransformer {

    public void transformPage(View view, float position) {
        DiscreteScrollView horizontalListings = HomeTabsFragment.getInstance().binding.horizontalRecyclerViewListings;

        if(position > 0){
            ViewGroup.LayoutParams layoutParams = horizontalListings.getLayoutParams();
            layoutParams.height = 0;
            horizontalListings.setLayoutParams(layoutParams);
        }else {
            ViewGroup.LayoutParams layoutParams = horizontalListings.getLayoutParams();
            layoutParams.height = 500;
            horizontalListings.setLayoutParams(layoutParams);
        }

//        if(position > 0 && position < 1) {
            //horizontalListings.setLayoutParams(new LinearLayout.LayoutParams(horizontalListings.getWidth(), horizontalListings.getHeight() * (int)Math.abs(position)));
//            horizontalListings.setScaleX(1 - Math.abs(position));
//            horizontalListings.setScaleY(1 - Math.abs(position));

//        ViewGroup.LayoutParams layoutParams = horizontalListings.getLayoutParams();
//        layoutParams.height = (1 - (int)Math.abs(position)) * 500;
            //horizontalListings.requestLayout();
//        horizontalListings.setLayoutParams(layoutParams);
//        }
        //Log.d("currentpage", (String) view.getTag());
            //horizontalListings.setScaleY( 1 - Math.abs(position));

//        }else{
//            horizontalListings.setScaleY(1 - Math.abs(position));
//        }

//        if (position < -1) { // [-Infinity,-1)
//            // This page is way off-screen to the left.
//            view.setAlpha(0f);
//
//        } else if (position <= 1) { // [-1,1]
//            // Modify the default slide transition to shrink the page as well
//            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
//            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
//            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
//            if (position < 0) {
//                view.setTranslationX(horzMargin - vertMargin / 2);
//            } else {
//                view.setTranslationX(-horzMargin + vertMargin / 2);
//            }
//
//            // Scale the page down (between MIN_SCALE and 1)
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
//
//            // Fade the page relative to its size.
//            view.setAlpha(MIN_ALPHA +
//                    (scaleFactor - MIN_SCALE) /
//                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));
//
//        } else { // (1,+Infinity]
//            // This page is way off-screen to the right.
//            view.setAlpha(0f);
//        }
    }
}
