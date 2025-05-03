package com.example.yadrotestovoe.presentation.screens.contactsScreen.components.success

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ContactFirstLetter(initial: Char) {
    Column {
        HorizontalDivider(
            color = Color.Blue, thickness = 1.dp, modifier =
                Modifier.padding(top = 8.dp)
        )
        Text(
            text = initial.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
        HorizontalDivider(
            color = Color.Blue, thickness = 1.dp, modifier =
                Modifier.padding(bottom = 8.dp)
        )
    }
}