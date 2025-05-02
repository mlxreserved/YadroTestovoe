package com.example.yadrotestovoe.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.core.content.ContextCompat
import com.example.yadrotestovoe.data.contact.local.repository.ContactLocalRepositoryImpl
import com.example.yadrotestovoe.presentation.ui.theme.YadroTestovoeTheme

class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Инициализация запроса разрешения
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Разрешение получено, можно продолжать работу
                setContent {
                    YadroTestovoeTheme {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        }
                    }
                }
            } else {
                // Разрешение отклонено, нужно отобразить пользователю соответствующее сообщение
                Toast.makeText(this, "Contact permission is required to use this feature.", Toast.LENGTH_SHORT).show()
            }
        }

        // Проверка и запрос разрешения на съемку
        checkAndRequestContactPermission()
    }

    private fun checkAndRequestContactPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED -> {

                // Разрешение уже получено, можно продолжать работу
                setContent {
                    YadroTestovoeTheme {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        }
                    }
                }
            }
            else -> {
                // Запрашиваем разрешение
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }
}