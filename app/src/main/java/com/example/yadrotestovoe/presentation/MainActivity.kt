package com.example.yadrotestovoe.presentation

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
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

    private var deleteDuplicateContacts: DeleteDuplicateContacts? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            deleteDuplicateContacts = DeleteDuplicateContacts.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            deleteDuplicateContacts = null
        }

    }

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
                    ContactsListScreen(deleteDuplicateContacts = deleteDuplicateContacts, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(createExplicitIntent(), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
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

    private fun createExplicitIntent(): Intent {
        val intent = Intent("com.example.aidl.REMOTE_CONNECTION")
        val services = packageManager.queryIntentServices(intent, 0)
        if (services.isEmpty()) {
            throw IllegalStateException("Приложение-сервер не установлено")
        }
        return Intent(intent).apply {
            val resolveInfo = services[0]
            val packageName = resolveInfo.serviceInfo.packageName
            val className = resolveInfo.serviceInfo.name
            component = ComponentName(packageName, className)
        }
    }


}