package com.example.yadrotestovoe.presentation.screens.contactsScreen.components.success

import android.content.Context
import android.provider.ContactsContract
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
    context: Context,
    contact: Contact
) {
    val typeLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(
        context.resources,
        contact.phoneType ?: 0,
        ""
    ).toString()
    val phoneNumberWithLabel = if(contact.phoneNumber != null) "${contact.phoneNumber} ($typeLabel)" else stringResource(R.string.empty_number)

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
                text = phoneNumberWithLabel,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}