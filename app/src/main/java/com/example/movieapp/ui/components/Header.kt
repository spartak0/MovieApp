package com.example.movieapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.R
import com.example.movieapp.ui.theme.spacing

const val HEADER_HEIGHT = 50

@Composable
fun Header(
    title: String,
    modifier: Modifier,
    searchBtnOnClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(HEADER_HEIGHT.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Title(
            text = title,
            modifier = Modifier
                .width(250.dp)
                .padding(bottom = spacing.small)
                .align(Alignment.BottomCenter),
        )
        IconBtn(
            modifier = Modifier
                .padding(end = spacing.medium)
                .align(Alignment.BottomEnd)
                .absoluteOffset(y = 4.dp),
            icon = R.drawable.baseline_search_24,
            onClick = searchBtnOnClick
        )

    }
}

@Composable
fun SearchHeader(
    modifier: Modifier,
    backOnClick: () -> Unit,
    text: String,
    textChange: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(HEADER_HEIGHT.dp)
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.Bottom
    ) {
        IconBtn(
            modifier = Modifier
                .padding(start = spacing.medium)
                .absoluteOffset(y = 4.dp),
            icon = R.drawable.baseline_arrow_back_24,
            onClick = backOnClick
        )
        Search(
            text = text,
            textChange = textChange,
            modifier = Modifier
                .padding(horizontal = spacing.medium, vertical = spacing.small)
                .fillMaxSize()
        )
    }
}

@Composable
fun Search(
    modifier: Modifier,
    text: String,
    textChange: (String) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.border(BorderStroke(1.dp, Color.White), RoundedCornerShape(8.dp))
    ) {
        BasicTextField(
            value = text,
            onValueChange = textChange,
            modifier = Modifier
                .padding(horizontal = spacing.small)
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            cursorBrush = SolidColor(Color.White),
            singleLine = true,
            maxLines=1,
        )
    }
}


@Composable
fun IconBtn(
    modifier: Modifier,
    @DrawableRes icon: Int,
    tint: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = icon),
            tint = tint,
            contentDescription = null
        )
    }
}

@Composable
fun Title(text: String, modifier: Modifier) {
    Text(
        text = text,
        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp),
        maxLines = 2,
        //overflow = TextOverflow.Ellipsis,
    )
}