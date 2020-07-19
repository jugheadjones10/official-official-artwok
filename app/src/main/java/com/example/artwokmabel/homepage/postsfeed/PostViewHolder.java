package com.example.artwokmabel.homepage.postsfeed;

import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.databinding.ItemPostBinding;

class PostViewHolder extends RecyclerView.ViewHolder {
    ItemPostBinding binding;

    public PostViewHolder(ItemPostBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
