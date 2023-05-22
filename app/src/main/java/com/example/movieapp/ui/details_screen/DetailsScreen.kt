package com.example.movieapp.ui.details_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.movieapp.R
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.ui.main_activity.SnackbarAction
import com.example.movieapp.ui.components.IconBtn
import com.example.movieapp.ui.main_screen.Poster
import com.example.movieapp.ui.theme.spacing

@Composable
fun DetailsScreen(
    movie: Movie,
    navigateUp: () -> Unit,
    showSnackbar: (String, SnackbarAction) -> Unit,
) {
    val widthPoster = LocalConfiguration.current.screenWidthDp + 1
    val heightPoster = widthPoster * 1.2
    Column {
        Box(
            modifier = Modifier
                .width(widthPoster.dp)
                .height(heightPoster.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Poster(
                imageUrl = movie.image ?: "",
                onError = { error -> showSnackbar(error, SnackbarAction.Error) },
                bigPoster = true,
                modifier = Modifier.fillMaxSize()
            )
            IconBtn(
                modifier = Modifier
                    .padding(
                        top = 24.dp + spacing.medium, start = spacing.medium
                    )
                    .align(Alignment.TopStart)
                    .border(
                        1.dp, Color.White, CircleShape
                    ),
                icon = R.drawable.baseline_arrow_back_24,
                tint = Color.White,
                onClick = navigateUp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(MaterialTheme.shapes.medium)
                .padding(spacing.medium)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = movie.title.toString(),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
            )
            Spacer(modifier = Modifier.height(spacing.small))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "RELEASED IN ${movie.releaseDate}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(text = "RATING: ${movie.rating}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(spacing.small))
            Text(text = movie.overview.toString(), style = MaterialTheme.typography.bodyMedium)

        }
    }
}