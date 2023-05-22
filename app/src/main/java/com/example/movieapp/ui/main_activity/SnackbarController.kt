package com.example.movieapp.ui.main_activity

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SnackbarController(
    private val scope: CoroutineScope,
    private val snackbarHostState: SnackbarHostState,
) {
    init {
        cancelActiveJob()
    }

    private var snackbarJob: Job? = null

    fun getSnackbarHostState() = snackbarHostState


    fun showSnackbar(
        message: String,
        action: SnackbarAction
    ) {
        snackbarJob = if (snackbarJob == null) {
            scope.launch {
                snackbarHostState.showSnackbar(message = message, action.name)
                cancelActiveJob()
            }
        } else {
            cancelActiveJob()
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = action.name,
                    duration = SnackbarDuration.Short
                )
                cancelActiveJob()
            }
        }
    }

    private fun cancelActiveJob() {
        snackbarJob?.let { job ->
            job.cancel()
            snackbarJob = Job()
        }
    }
}