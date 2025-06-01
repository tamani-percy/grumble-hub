package com.example.grumblehub.features.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.example.grumblehub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    placeholderText: String = "Search...",
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        DockedSearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch(it)
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { newExpandedState -> expanded = newExpandedState },
                    placeholder = { Text(placeholderText) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_search_24),
                            contentDescription = null
                        )
                    },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            if (searchResults.isEmpty() && query.isNotBlank()) {
                // Show "No results" only when there's a query but no results
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No results found for \"$query\"")
                }
            } else if (searchResults.isEmpty() && query.isBlank() && expanded) {
                // Show "Start typing" when active but no query
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Start typing to search...")
                }
            } else {
                // Display search results in a scrollable column
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { result ->
                        ListItem(
                            headlineContent = { Text(result) },
                            modifier = Modifier
                                .clickable {
                                    // When a result is clicked:
                                    onQueryChange(result) // Update the query with the selected result
                                    onSearch(result) // Trigger search with the selected result
                                    expanded = false // Collapse the search bar
                                }
                                .fillMaxWidth()
                        )
                        Divider() // Optional: separator between list items
                    }
                }
            }
        }
    }
}
