package com.example.artwokmabel.homepage.homepagestuff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.models.Category;
import com.example.artwokmabel.R;

import java.util.List;
import java.util.Objects;

public class TabsPagerRecyclerAdapter extends RecyclerView.Adapter<TabsPagerRecyclerAdapter.TabsHolder>{
    private List<Category> categoriesList;


    @NonNull
    @Override
    public TabsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_posts,viewGroup,false);

        return new TabsHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TabsHolder myHolder, int i) {
        Category data = categoriesList.get(i);

    }

    public void setCategoriesList(final List<Category> categoriesList) {
        if (this.categoriesList == null) {
            this.categoriesList = categoriesList;

            notifyItemRangeInserted(0, categoriesList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return TabsPagerRecyclerAdapter.this.categoriesList.size();
                }

                @Override
                public int getNewListSize() {
                    return categoriesList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return TabsPagerRecyclerAdapter.this.categoriesList.get(oldItemPosition).getName()
                            .equals(categoriesList.get(newItemPosition).getName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Category project = categoriesList.get(newItemPosition);
                    Category old = categoriesList.get(oldItemPosition);
                    return project.getName() == old.getName()
                            && Objects.equals(project, old);
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

    class TabsHolder extends RecyclerView.ViewHolder {

        public TabsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

