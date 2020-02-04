package com.example.artwokmabel.homepage.search.listings

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import kotlinx.android.synthetic.main.list_item_small.view.*

class SearchListingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(myListing: SearchListing) {
        view.itemName.text = myListing.highlightedName?.toSpannedString() ?: myListing.itemname
    }
}