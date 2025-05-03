package com.example.yadrotestovoe.presentation.screens.contactsScreen.composable.success

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.yadrotestovoe.R
import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.presentation.screens.contactsScreen.ContactsViewModel


@Composable
fun SuccessContactsScreen(
    groupedContacts: Map<Char, List<Contact>>,
    contactsViewModel: ContactsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val errorNumber = stringResource(R.string.error_empty_number)

    val errorCallPermission = stringResource(R.string.error_call_permission)
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Обработка результата через ViewModel
        contactsViewModel.handlePermissionResult(context, errorCallPermission, isGranted)
    }

    LazyColumn {
        groupedContacts.forEach { (initial, contactsInGroup) ->
            item {
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
            items(contactsInGroup) { contact ->
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                        .clickable {
                            contactsViewModel.onContactPressed(
                                context = context,
                                contact = contact,
                                requestPermissionLauncher,
                                error = errorNumber
                            )
                        }
                        .border(BorderStroke(1.dp, Color.Blue))
                ) {
                    if(contact.thumbnailUri != null){
                        AsyncImage(
                            model = contact.thumbnailUri,
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
                    ContactInfo(context = context, contact = contact)
                }
            }
        }
    }
}
