package com.example.artwokmabel.homepage.fragments.Listingssearch

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject

data class SearchListing(
        val itemname: String,
        override val _highlightResult: JsonObject?
) : Highlightable {

    @Transient
    public val highlightedName: HighlightedString?
        get() = getHighlight(Attribute("itemname"))
}