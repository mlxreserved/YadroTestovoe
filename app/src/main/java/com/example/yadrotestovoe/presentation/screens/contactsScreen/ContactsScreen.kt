package com.example.yadrotestovoe.presentation.screens.contactsScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yadrotestovoe.presentation.screens.contactsScreen.composable.success.SuccessContactsScreen

@Composable
fun ContactsListScreen(
    modifier: Modifier = Modifier,
    contactsViewModel: ContactsViewModel = hiltViewModel()
) {
    val contactsState by contactsViewModel.contactsState.collectAsState()

    Box(modifier = modifier) {
         when (val state = contactsState) {
            ContactsScreenState.Empty -> {}
            is ContactsScreenState.Error -> {}
            ContactsScreenState.Loading -> {

            }

            is ContactsScreenState.Success -> SuccessContactsScreen(
                groupedContacts = state.contacts,
                contactsViewModel = contactsViewModel
            )
        }
    }

}
