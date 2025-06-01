package com.example.grumblehub.features.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.grumblehub.features.search.components.SimpleSearchBar

@Composable
fun SearchScreen(modifier: Modifier, navController: NavController) {

//    val allItems = listOf(
//        "Apple", "Banana", "Cherry", "Date", "Elderberry",
//        "Fig", "Grape", "Honeydew", "Jackfruit", "Kiwi"
//    )
//
//    fun performSearch(query: String) {
//        results = if (query.isBlank()) {
//            emptyList()
//        } else {
//            allItems.filter { it.contains(query, ignoreCase = true) }
//        }
//    }
    SimpleSearchBar(
        modifier = modifier,
        query = "",
        onQueryChange = {},
        onSearch = {},
        searchResults = emptyList(),
        placeholderText = "Search grievances..."
    )
}