package com.example.movieapp.ui.main_activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.movieapp.ui.navigation.NavGraph
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.spacing
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            val snackbarController = remember { SnackbarController(scope, SnackbarHostState()) }
            WindowCompat.setDecorFitsSystemWindows(window,false)
            MovieAppTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(snackbarController.getSnackbarHostState()) {
                            MySnackbar(
                                modifier = Modifier
                                    .padding(
                                        bottom = spacing.large,
                                        start = spacing.medium,
                                        end = spacing.medium
                                    )
                                    .fillMaxWidth()
                                    .height(50.dp),
                                message = it.visuals.message,
                                action = it.visuals.actionLabel

                            )
                        }
                    },
                ) {
                    NavGraph(showSnackbar = { message, action ->
                        scope.launch {
                            snackbarController.showSnackbar(message, action)
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun MySnackbar(modifier: Modifier, message: String, action: String? = null) {
    val snackbarColor: Color = when (action) {
        SnackbarAction.Error.name -> MaterialTheme.colorScheme.error
        else -> {
            MaterialTheme.colorScheme.primaryContainer
        }
    }
    val textColor = when (action) {
        SnackbarAction.Error.name -> MaterialTheme.colorScheme.onError
        else -> {
            MaterialTheme.colorScheme.onPrimaryContainer
        }
    }
    Snackbar(modifier = modifier, containerColor = snackbarColor) {
        Text(
            text = message,
            color = textColor,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = spacing.small)
                .fillMaxWidth()
        )
    }
}