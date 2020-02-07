package com.algolia.instantsearch.guides.gettingstarted

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.example.artwokmabel.HomePageActivity
import com.example.artwokmabel.homepage.user.IndivUserFragment
import kotlinx.android.synthetic.main.list_item_small.view.*


class ProductViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(product: Product) {
        view.itemName.text = product.highlightedName?.toSpannedString() ?: product.name
        view.single_search_res.setOnClickListener{

            val indivUserFrag = IndivUserFragment()
            val args = Bundle()
            args.putString("poster_username", product.name);
            indivUserFrag.arguments = args;
            HomePageActivity.getInstance().loadFragment(indivUserFrag);
        }
    }
}