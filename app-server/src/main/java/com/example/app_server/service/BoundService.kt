package com.example.app_server.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.example.aidl.AsyncCallback
import com.example.aidl.CustomAidlException
import com.example.aidl.DeleteDuplicateContacts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoundService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onBind(intent: Intent?): IBinder? {
        return object : DeleteDuplicateContacts.Stub() {
            override fun deleteDuplicateContacts(callback: AsyncCallback?) {
                serviceScope.launch {
                    try {
                        val result = performDeleteDuplicates()
                        // Вернуться в main-поток, чтобы безопасно вызвать callback
                        withContext(Dispatchers.Main) {
                            callback?.onSuccess(result)
                        }
                    } catch (e: Throwable) {
                        Log.e("BoundService", e.message, e)
                        if (e is RuntimeException) {
                            val customAidlException = CustomAidlException(e.message)
                            callback?.onError(customAidlException)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel() // Отменяем все запущенные корутины при уничтожении сервиса
    }

    // Симуляция тяжёлой операции
    private suspend fun performDeleteDuplicates(): String {
        // Например, здесь может быть доступ к базе данных, контактам и т. д.
        // Для примера просто задержка:
        kotlinx.coroutines.delay(2000) // 2 секунды «работаем»

        return "All Successfully"
    }
}