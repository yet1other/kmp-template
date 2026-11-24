package com.yet.template.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.yet.template.feature.home.HomeComponent
import com.yet.template.feature.home.PreviewHomeComponent
import com.yet.template.resources.AppTheme
import com.yet.template.resources.Res
import com.yet.template.resources.home_add
import com.yet.template.resources.home_delete
import com.yet.template.resources.home_empty
import com.yet.template.resources.home_input_hint
import com.yet.template.resources.home_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeContent(component: HomeComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(Res.string.home_title)) }) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 16.dp,
            ),
        ) {
            item {
                NoteInput(
                    text = model.text,
                    canAdd = model.canAdd,
                    onTextChanged = component::onTextChanged,
                    onAdd = component::onAddClicked,
                )
            }
            if (model.notes.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(Res.string.home_empty),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            items(model.notes, key = { it.id.value }) { note ->
                ListItem(
                    headlineContent = { Text(note.text) },
                    trailingContent = {
                        TextButton(onClick = { component.onDeleteClicked(note.id) }) {
                            Text(stringResource(Res.string.home_delete))
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun NoteInput(
    text: String,
    canAdd: Boolean,
    onTextChanged: (String) -> Unit,
    onAdd: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            placeholder = { Text(stringResource(Res.string.home_input_hint)) },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
        Button(onClick = onAdd, enabled = canAdd) {
            Text(stringResource(Res.string.home_add))
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    AppTheme {
        HomeContent(PreviewHomeComponent())
    }
}
