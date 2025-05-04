package com.example.yadrotestovoe.presentation.screens.contactsScreen.components.success

import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.example.aidl.AsyncCallback
import com.example.aidl.CustomAidlException
import com.example.aidl.DeleteDuplicateContacts
import com.example.yadrotestovoe.R
import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.presentation.screens.contactsScreen.ContactsViewModel


@Composable
fun SuccessContactsScreen(
    deleteDuplicateContacts: DeleteDuplicateContacts?,
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

    Column {
        // Отображение списка контактов

        Button(
            onClick = {
                deleteDuplicateContacts?.deleteDuplicateContacts(object : AsyncCallback.Stub() {
                    override fun onSuccess(successMessage: String?) {
                        Log.d("SUCCESS", successMessage.toString())
                    }

                    override fun onEmpty(emptyMessage: String?) {
                        Log.d("EMPTY", emptyMessage.toString())

                        Toast.makeText(context, emptyMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(customAidlException: CustomAidlException?) {
                        Log.d("ERROR", customAidlException.toString())

                        Toast.makeText(context, customAidlException.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        ){
            Text(text = "Click me")
        }
    }
}
