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
                }
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
fun AnimationSelector(
    selectedStyle: DetailScreenAnimation,
    onStyleSelect: (DetailScreenAnimation) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val styles = DetailScreenAnimation.values()
        styles.forEach { style ->
            Row(
                Modifier
                    .clickable { onStyleSelect(style) }
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedStyle == style,
                    onClick = { onStyleSelect(style) }
                )
                val (icon, text) = when (style) {
                    DetailScreenAnimation.STYLE_ONE -> Icons.Default.Star to "Style 1"
                    DetailScreenAnimation.STYLE_TWO -> Icons.Default.CheckCircle to "Style 2"
                }
                Icon(icon, contentDescription = text)
                Spacer(Modifier.width(4.dp))
                Text(text)
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CharacterDetailCardAnimated(character: MovieCharacterDetailUiModel) {
    var visible by remember { mutableStateOf(false) }

    // Trigger animation when composable enters composition
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)),
        exit = slideOutVertically(animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top colored image section with icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Character Image",
                        modifier = Modifier.size(100.dp),
                        tint = Color.White
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = character.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailText(label = "Gender", value = character.gender)
                    DetailText(label = "Culture", value = character.culture)
                    DetailText(label = "Born", value = character.born)
                    DetailText(label = "Died", value = character.died)
                    DetailText(label = "Titles", value = character.titles)
                    DetailText(label = "Aliases", value = character.aliases)
                    DetailText(label = "TV Series", value = character.tvSeries)
                    DetailText(label = "Played By", value = character.playedBy)
                }
            }
        }
    }
}


@Composable
fun WiggleCharacterCard(character: MovieCharacterDetailUiModel) {
    var wiggleTrigger by remember { mutableStateOf(false) }

    // Define rotation animation
    val rotation by animateFloatAsState(
        targetValue = if (wiggleTrigger) 10f else 0f, // rotate 10 degrees
        animationSpec = keyframes {
            durationMillis = 300
            0f at 0
            10f at 50
            -10f at 100
            10f at 150
            -10f at 200
            5f at 250
            -5f at 280
            0f at 300
        },
        finishedListener = { wiggleTrigger = false }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .rotate(rotation) // apply rotation
            .clickable { wiggleTrigger = true } // trigger wiggle on click
            .shadow(12.dp, shape = MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Character Image",
                    modifier = Modifier.size(100.dp),
                    tint = Color.White
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = character.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(12.dp))
                DetailText(label = "Gender", value = character.gender)
                DetailText(label = "Culture", value = character.culture)
                DetailText(label = "Born", value = character.born)
                DetailText(label = "Died", value = character.died)
            }
        }
    }
}
