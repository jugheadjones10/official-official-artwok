package com.example.artwokmabel.homepage.postsfeed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class SmartNestedScrollView extends NestedScrollView {
    public SmartNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public SmartNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);

        if (findNestedRecyclerView(child) != null) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(lp.topMargin + lp.bottomMargin, MeasureSpec.AT_MOST);
            child.measure(parentWidthMeasureSpec, childHeightMeasureSpec);
        } else {
            super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        }
    }

    private RecyclerView findNestedRecyclerView(View view){
        if(view instanceof RecyclerView){
            return (RecyclerView)view;
        }else if(view instanceof ViewGroup){
            int index = 0;
            while(index < ((ViewGroup) view).getChildCount()){
                View child = ((ViewGroup) view).getChildAt(index);
                RecyclerView recyclerView = findNestedRecyclerView(child);
                if(recyclerView == null) {
                    index += 1;
                }else{
                    return recyclerView;
                }
            }
        }
        return null;
    }
}
