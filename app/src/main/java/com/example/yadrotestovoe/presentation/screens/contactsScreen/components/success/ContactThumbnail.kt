package com.example.yadrotestovoe.presentation.screens.contactsScreen.components.success

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ContactThumbnail(thumbnailUri: String?) {
    if(thumbnailUri != null){
        AsyncImage(
            model = thumbnailUri,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .padding(4.dp)
                .clip(CircleShape)
        )
    } else {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}