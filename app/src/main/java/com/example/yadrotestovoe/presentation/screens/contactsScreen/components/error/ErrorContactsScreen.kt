package com.example.yadrotestovoe.presentation.screens.contactsScreen.components.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.yadrotestovoe.R
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ErrorContactsScreen(
    modifier: Modifier = Modifier
) {
    val errorText = stringResource(R.string.error_load_contacts)

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = errorText,
            textAlign = TextAlign.Center
        )
    }
}