package com.deontch.characterslist.presentation

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deontch.base.contract.ViewEvent
import com.deontch.base.model.MessageState
import com.deontch.common.Ignored
import com.deontch.common.displayToast
import com.deontch.characterslist.domain.contract.MovieCharacterSideEffect
import com.deontch.characterslist.domain.contract.MovieCharacterViewAction
import com.deontch.characterslist.presentation.component.LoadingView
import com.deontch.characterslist.presentation.model.MovieCharacterUiModel
import com.deontch.ui.extensions.collectAllEffect
import com.deontch.ui.extensions.rememberStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
object MovieCharacterScreenDestination

@Composable
fun MovieCharacterScreen(onCharacterCardClick: (String) -> Unit) {
    val viewModel: MovieCharactersListViewModel = hiltViewModel()
    val state by rememberStateWithLifecycle(viewModel.state)
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val activity = context as? Activity
    val errorMessage = state.errorState as? MessageState.Inline

    SubscribeToSideEffects(
        events = viewModel.events,
        context = context
    )

    // Trigger initial load
    LaunchedEffect(Unit) {
        viewModel.onViewAction(MovieCharacterViewAction.GetCharacters)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(colors = listOf(Color(0xFF81D4FA), Color(0xFF03A9F4))))
    ) {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { viewModel.onViewAction(MovieCharacterViewAction.UpdateSearchQuery(it)) },
            onSearch = { viewModel.onViewAction(MovieCharacterViewAction.SearchCharacters) }
        )

        when {
            state.isLoading -> LoadingView()
            errorMessage != null -> ErrorView(errorMessage)
            state.searchQuery.isNotEmpty() -> CharacterList(
                state.searchQueryResponse,
                listState,
                onCharacterCardClick
            )

            else -> CharacterList(state.characterList, listState, onCharacterCardClick)
//            TODO(Fix this)
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        label = { Text("Search Characters", color = Color.White.copy(alpha = 0.7f)) },        singleLine = true,
        textStyle = TextStyle(color = Color.White),
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White.copy(alpha = 0.8f),
            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
            cursorColor = Color.White
        )
    )
}

@Composable
fun CharacterList(
    characters: List<MovieCharacterUiModel>,
    listState: LazyListState,
    onCharacterCardClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = listState
    ) {
        items(
            characters,
            key = { "${it.id}-${it.name}" }
        ) { character ->
            CharacterCard(character, onClick = onCharacterCardClick)
        }
    }
}

@Composable
fun CharacterCard(
    character: MovieCharacterUiModel,
    onClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick.invoke(character.name) }
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(
                    radius = 24.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .background(Color.White.copy(alpha = 0.15f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.2f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                )
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Culture: ${character.culture}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Death: ${character.deathInfo}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = character.seasonsAppeared,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
@Composable
fun ErrorView(messageState: MessageState.Inline) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = messageState.message,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun SubscribeToSideEffects(
    events: Flow<ViewEvent>, context: Context
) {
    LaunchedEffect(events) {
        events.collectAllEffect<MovieCharacterSideEffect> { effect ->
            when (effect) {
                is MovieCharacterSideEffect.ShowErrorMessage -> {
                    when (effect.errorMessageState) {
                        is MessageState.Toast -> {
                            displayToast(context, effect.errorMessageState.message)
                        }

                        else -> Ignored
                    }
                }
            }
        }
    }
}
