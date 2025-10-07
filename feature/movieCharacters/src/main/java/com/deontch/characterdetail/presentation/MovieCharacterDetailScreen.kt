package com.deontch.characterdetail.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.deontch.characterdetail.domain.contract.MovieCharacterDetailViewAction
import com.deontch.characterdetail.presentation.components.CardCharacterDetailWithAnimation
import com.deontch.ui.extensions.rememberStateWithLifecycle
import kotlinx.serialization.Serializable

@Serializable
data class MovieCharacterDetailScreenDestination(val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCharacterDetailScreen(
    characterName: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    val viewModel: MovieCharacterDetailViewModel = hiltViewModel()
    val state by rememberStateWithLifecycle(viewModel.state)

    LaunchedEffect(Unit) {
        viewModel.onViewAction(MovieCharacterDetailViewAction.GetMovieCharacterDetail(characterName))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
            )
        },
        containerColor = Color(0xFF81D4FA)
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.characterDetail != null -> {
                    state.characterDetail?.let {
                        CardCharacterDetailWithAnimation(character = it)
                    }
                }

                else -> {
                    Text(
                        text = "Character details not available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
