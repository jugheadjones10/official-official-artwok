package com.example.artwokmabel.homepage.fragments.Listingssearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.artwokmabel.R


class SearchListingsAdapter: PagedListAdapter<SearchListing, SearchListingViewHolder>(SearchListingsAdapter){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_small, parent, false)
        return SearchListingViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListingViewHolder, position: Int) {
        val product = getItem(position)
        if (product != null) holder.bind(product)
    }

    companion object : DiffUtil.ItemCallback<SearchListing>() {

        override fun areItemsTheSame(
                oldItem: SearchListing,
                newItem: SearchListing
        ): Boolean {
            return oldItem::class == newItem::class
        }

        override fun areContentsTheSame(
                oldItem: SearchListing,
                newItem: SearchListing
        ): Boolean {
            return oldItem.itemname == newItem.itemname
        }
    }
}