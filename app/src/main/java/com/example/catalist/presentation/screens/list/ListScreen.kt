package com.example.catalist.presentation.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


data class ListScreenState(
    val items: List<CatListItemState> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
)

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    state: ListScreenState,
    onItemClicked: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = state.searchQuery,
            onValueChange = { query ->
                onSearchQueryChanged(query)
            }
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                items(state.items) { catListItem ->
                    CatListItem(
                        state = catListItem,
                        modifier = Modifier.fillMaxWidth().clickable {
                            onItemClicked(catListItem.id)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                }

            }
        }

    }
}



