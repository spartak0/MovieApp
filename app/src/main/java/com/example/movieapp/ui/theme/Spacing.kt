package com.example.movieapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SpacingValue {
    val extraSmall: Dp = 4.dp
    val small: Dp = 8.dp
    val medium: Dp = 16.dp
    val large: Dp = 32.dp
    val extraLarge: Dp = 32.dp
}

val LocalSpacing = compositionLocalOf { SpacingValue }

val spacing: SpacingValue
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
