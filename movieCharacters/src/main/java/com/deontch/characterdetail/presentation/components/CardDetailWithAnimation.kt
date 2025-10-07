package com.deontch.characterdetail.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deontch.characterdetail.presentation.model.MovieCharacterDetailUiModel

@Composable
fun InspectableCard(
    modifier: Modifier = Modifier,
    character: MovieCharacterDetailUiModel
) {
    val localDensity = LocalDensity.current

    var isFlipped by rememberSaveable { mutableStateOf(false) }
    var rotationX by remember { mutableFloatStateOf(0f) }
    var rotationY by remember { mutableFloatStateOf(0f) }

    var cardWidth by remember { mutableFloatStateOf(0f) }
    var cardHeight by remember { mutableFloatStateOf(0f) }

    val flipRotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "flipRotation"
    )

    val animatedRotationX by animateFloatAsState(
        targetValue = rotationX, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ), label = "rotationX"
    )

    val animatedRotationY by animateFloatAsState(
        targetValue = rotationY, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ), label = "rotationY"
    )

    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .onGloballyPositioned { coordinates ->
                    cardWidth = coordinates.size.width.toFloat()
                    cardHeight = coordinates.size.height.toFloat()
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            rotationX = 0f
                            rotationY = 0f
                        },
                        onDragCancel = {
                            rotationX = 0f
                            rotationY = 0f

                        },
                    ) { change, dragAmount ->

                        change.consume()

                        val currentTouchX = change.position.x
                        val currentTouchY = change.position.y

                        val normalizedX = (currentTouchX - cardWidth / 2f) / (cardWidth / 2f)
                        val normalizedY = (currentTouchY - cardHeight / 2f) / (cardHeight / 2f)

                        val maxRotation = 25f

                        rotationX = -normalizedY * maxRotation
                        rotationY = normalizedX * maxRotation
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            isFlipped = !isFlipped
                        })
                }
                .graphicsLayer {
                    this.rotationY = flipRotation + animatedRotationY
                    this.rotationX = animatedRotationX
                    cameraDistance = 12f * localDensity.density
                }, contentAlignment = Alignment.Center
        ) {
            CharacterCard(character)
        }
    }
}

@Composable
fun CharacterCard(character: MovieCharacterDetailUiModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF81D4FA), Color(0xFF03A9F4))
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .blur(radius = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(24.dp)
                )
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.15f)
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(0.dp)
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
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.25f),
                                    Color.White.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Character Image",
                        modifier = Modifier.size(100.dp),
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = character.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailTextRow(label = "Gender", value = character.gender)
                    DetailTextRow(label = "Culture", value = character.culture)
                    DetailTextRow(label = "Born", value = character.born)
                    DetailTextRow(label = "Died", value = character.died)
                    DetailTextRow(label = "Titles", value = character.titles)
                    DetailTextRow(label = "Aliases", value = character.aliases)
                    DetailTextRow(label = "TV Series", value = character.tvSeries)
                    DetailTextRow(label = "Played By", value = character.playedBy)
                }
            }
        }
    }
}

@Composable
private fun DetailTextRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}
