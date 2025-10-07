package com.deontch.characterdetail.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deontch.characterdetail.domain.contract.MovieCharacterDetailViewAction
import com.deontch.characterdetail.presentation.components.InspectableCard
import com.deontch.characterdetail.presentation.model.MovieCharacterDetailUiModel
import com.deontch.ui.extensions.clickable
import com.deontch.ui.extensions.rememberStateWithLifecycle
import kotlinx.serialization.Serializable

enum class DetailScreenAnimation {
    STYLE_ONE,
    STYLE_TWO
}

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
                            imageVector = Icons.Default.Person,
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = Modifier.background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF81D4FA), Color(0xFF81D4FA))
                    )
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
            )
        }
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
                        InspectableCard(character = it)
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

@Composable
private fun DetailText(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}
