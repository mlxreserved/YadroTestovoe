package com.example.yadrotestovoe.presentation

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.aidl.DeleteDuplicateContacts
import com.example.yadrotestovoe.presentation.screens.contactsScreen.ContactsListScreen
import com.example.yadrotestovoe.presentation.ui.theme.YadroTestovoeTheme
import com.example.yadrotestovoe.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Инициализация запроса разрешения
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (!isGranted) {
                    // Разрешение отклонено, нужно отобразить пользователю соответствующее сообщение
                    Toast.makeText(
                        this,
                        getString(R.string.error_contacts_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        // Проверка и запрос разрешения на чтение контактов
        checkAndRequestContactPermission()

        // Разрешение получено, можно продолжать работу
        setContent {
            YadroTestovoeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContactsListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun checkAndRequestContactPermission() {
        if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED) {
            // Запрашиваем разрешение
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }
}