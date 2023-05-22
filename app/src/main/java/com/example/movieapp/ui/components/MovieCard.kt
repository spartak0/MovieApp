package com.example.movieapp.ui.main_screen

import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.movieapp.R
import com.example.movieapp.ui.components.shimmerEffect


@Composable
fun MovieCard(
    width: Dp,
    height: Dp,
    name: String,
    year: String,
    imageUrl: String,
    preloadRequest: RequestBuilder<Drawable>,
    onError: (String) -> Unit,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
    ) {
        Poster(
            imageUrl = imageUrl,
            preloadRequest = preloadRequest,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .weight(1f),
            onError = onError
        )
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = year, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun EmptyMovieCard(
    width: Dp,
    height: Dp,
    color: Color = MaterialTheme.colorScheme.secondaryContainer
) {
    Column(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect(backgroundColor = color)
                .fillMaxWidth()
                .weight(1f)
        )
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect(backgroundColor = color)
                .height(24.dp - 6.dp)
                .fillMaxWidth(),
        )
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect(backgroundColor = color)
                .height(24.dp - 6.dp)
                .width(50.dp),
        )

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Poster(
    imageUrl: String,
    preloadRequest: RequestBuilder<Drawable>? = null,
    onError: (String) -> Unit,
    bigPoster: Boolean = false,
    modifier: Modifier,
) {
    GlideImage(
        model = imageUrl,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        contentDescription = null,
    ) {
        it.placeholder(if (!bigPoster) R.drawable.app_icon_insert_2 else R.drawable.app_icon_insert_3)
            .thumbnail(preloadRequest)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    e?.message?.let { msg -> onError(msg) }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
    }
}