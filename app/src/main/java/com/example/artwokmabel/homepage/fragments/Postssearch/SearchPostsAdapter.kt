package com.algolia.instantsearch.guides.gettingstarted

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
//import com.algolia.instantsearch.guides.R
import com.example.artwokmabel.R


class SearchPostsAdapter: PagedListAdapter<SearchPost, SearchPostViewHolder>(SearchPostsAdapter){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_small, parent, false)
        return SearchPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchPostViewHolder, position: Int) {
        val product = getItem(position)
        if (product != null) holder.bind(product)
    }

    companion object : DiffUtil.ItemCallback<SearchPost>() {

        override fun areItemsTheSame(
                oldItem: SearchPost,
                newItem: SearchPost
        ): Boolean {
            return oldItem::class == newItem::class
        }

        override fun areContentsTheSame(
                oldItem: SearchPost,
                newItem: SearchPost
        ): Boolean {
            return oldItem.postdesc == newItem.postdesc
        }
    }
}