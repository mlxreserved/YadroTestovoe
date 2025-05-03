package com.example.yadrotestovoe.presentation.screens.contactsScreen.components.empty

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.yadrotestovoe.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun EmptyContactsScreen(modifier: Modifier = Modifier) {
    val emptyNotify = stringResource(R.string.empty_notify)

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = emptyNotify,
            textAlign = TextAlign.Center
        )
    }
}