package com.example.artwokmabel.homepage.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.CatItemCardBinding;
import com.example.artwokmabel.homepage.fragments.EditCategoriesFragment;
import com.example.artwokmabel.homepage.callbacks.CategoryClickCallback;
import com.example.artwokmabel.homepage.models.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatCardsAdapter extends RecyclerView.Adapter<CatCardsAdapter.CatHolder> {

    private List<Category> categories;

    public CatCardsAdapter() {

    }

    @NonNull
    @Override
    public CatHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        CatItemCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cat_item_card, parent, false);

        return new CatHolder(binding);
    }

    private final CategoryClickCallback categoryClickCallback = new CategoryClickCallback() {
        @Override
        public void onClick(Category category) {
            Log.d("myclick", "I got clicked!!!");
            category.setChecked(!category.isChecked());
            EditCategoriesFragment.getInstance().adapter.notifyDataSetChanged();
            //There should be a better way of doing the above
        }
    };

    @Override
    public void onBindViewHolder(@NonNull CatHolder holder, int position) {
        holder.binding.setCallback(categoryClickCallback);
        holder.binding.setCategory(categories.get(position));

        Picasso.get().load(categories.get(position).getCategoryPhoto()).into(holder.binding.catImage);

        holder.binding.tickView.bringToFront();

        if(categories.get(position).isChecked()){
            holder.binding.tickView.setVisibility(View.VISIBLE);
        }else{
            holder.binding.tickView.setVisibility(View.INVISIBLE);
        }

        holder.binding.executePendingBindings();
    }


    public List<Category> getCategoriesList(){
        return categories;
    }

    public void setCategoriesList(final List<Category> categories) {
        if (this.categories == null) {
            this.categories = categories;
            notifyItemRangeInserted(0, categories.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return CatCardsAdapter.this.categories.size();
                }

                @Override
                public int getNewListSize() {
                    return categories.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return CatCardsAdapter.this.categories.get(oldItemPosition).getName() ==
                            categories.get(newItemPosition).getName();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return CatCardsAdapter.this.categories.get(oldItemPosition).getName() ==
                            categories.get(newItemPosition).getName();
                }
            });
            this.categories = categories;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    static class CatHolder extends RecyclerView.ViewHolder {

        CatItemCardBinding binding;

        public CatHolder(CatItemCardBinding binding) {
           super(binding.getRoot());
           this.binding = binding;
        }

    }
}
