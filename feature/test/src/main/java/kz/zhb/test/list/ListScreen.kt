package kz.zhb.test.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kz.zhb.elm.CollectEffects
import kz.zhb.elm.collectState
import org.koin.compose.viewmodel.koinViewModel
import kz.zhb.test.model.Item

@Composable
fun ListScreen(
    onItemClick: (Item) -> Unit,
    viewModel: ListViewModel = koinViewModel(),
) {
    val state by viewModel.collectState()

    viewModel.CollectEffects { effect ->
        when (effect) {
            is ListEffect.OpenDetail -> onItemClick(effect.item)
            is ListEffect.ShowError -> println(effect.message)
        }
    }

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(Modifier.fillMaxSize()) {
        items(state.items, key = { it.id }) { item ->
            ListItem(
                headlineContent = { Text(item.title) },
                supportingContent = { Text(item.description) },
                modifier = Modifier.clickable { viewModel.accept(ListEvents.UI.ItemClicked(item)) },
            )
            HorizontalDivider()
        }
    }
}
