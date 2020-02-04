package com.algolia.instantsearch.guides.gettingstarted

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.helper.android.filter.state.connectPagedList
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.example.artwokmabel.homepage.search.Listingssearch.MyFacetListViewHolder
import io.ktor.client.features.logging.LogLevel


class SearchPostViewModel : ViewModel() {

    val client = ClientSearch(ApplicationID("CTIOUIUY3T"), APIKey("7f3f4f1b7f3eab10acf7e980b2023a23"), LogLevel.ALL)
    val index = client.initIndex(IndexName("Posts"))
    val searcher = SearcherSingleIndex(index)

    val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher) { hit ->
        SearchPost(
            hit.json.getPrimitive("postdesc").content,
            hit.json.getObjectOrNull("_highlightResult")
        )
    }
    val pagedListConfig = PagedList.Config.Builder().setPageSize(50).setEnablePlaceholders(false).build()
    val products: LiveData<PagedList<SearchPost>> = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    val searchBox = SearchBoxConnectorPagedList(searcher, listOf(products))

    val adapterProduct = SearchPostsAdapter()
    val stats = StatsConnector(searcher)

    val filterState = FilterState()
    val facetList = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = Attribute("category"),
        selectionMode = SelectionMode.Single
    )
    val facetPresenter = FacetListPresenterImpl(
        sortBy = listOf(FacetSortCriterion.CountDescending, FacetSortCriterion.IsRefined),
        limit = 100
    )
    val adapterFacet = FacetListAdapter(MyFacetListViewHolder.Factory)
    val connection = ConnectionHandler()

    init {
        connection += searchBox
        connection += stats
        connection += facetList
        connection += searcher.connectFilterState(filterState)
        connection += facetList.connectView(adapterFacet, facetPresenter)
        connection += filterState.connectPagedList(products)
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connection.disconnect()
    }
}