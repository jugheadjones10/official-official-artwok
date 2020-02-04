package com.example.artwokmabel.homepage.search.listings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.item.StatsTextView
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import com.algolia.instantsearch.helper.stats.connectView
import com.example.artwokmabel.R
import kotlinx.android.synthetic.main.fragment_search_users.*

class ListingsSearchFragment constructor(val searchView: SearchView) : Fragment() {

    private val connection = ConnectionHandler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_listings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(requireActivity())[SearchListingViewModel::class.java]
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        val statsView = StatsTextView(stats)

        viewModel.products.observe(this, Observer { hits -> viewModel.adapterProduct.submitList(hits) })

        connection += viewModel.searchBox.connectView(searchBoxView)
        connection += viewModel.stats.connectView(statsView, StatsPresenterImpl())

        productList.let {
            it.itemAnimator = null
            it.adapter = viewModel.adapterProduct
            it.layoutManager = LinearLayoutManager(requireContext())
            it.autoScrollToStart(viewModel.adapterProduct)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.disconnect()
    }
}