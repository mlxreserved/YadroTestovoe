package com.example.yadrotestovoe.presentation.screens.contactsScreen.components.success

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.yadrotestovoe.R
import com.example.yadrotestovoe.domain.model.Contact

@Composable
fun ContactInfo(
    contact: Contact
) {
    // Приводим номер и тип номера в удобный формат
    val phoneNumberWithLabel =
        if (contact.phoneNumber != null) "${contact.phoneNumber} (${contact.phoneType})" else null

    Column {
        Text(
            text = contact.name ?: stringResource(R.string.empty_name),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
        Row {
            Text(
                text = phoneNumberWithLabel ?: stringResource(R.string.empty_number),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}