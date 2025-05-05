package com.example.yadrotestovoe.data.contact.local.repository

import android.content.Intent
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.example.aidl.AsyncCallback
import com.example.aidl.CustomAidlException
import com.example.aidl.DeleteDuplicateContacts
import com.example.yadrotestovoe.domain.repository.AidlRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AidlRepositoryImpl(private val context: Context) : AidlRepository {

    private var deleteDuplicateContacts: DeleteDuplicateContacts? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            deleteDuplicateContacts = DeleteDuplicateContacts.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            deleteDuplicateContacts = null
        }

    }

    override suspend fun bindService() = withContext(Dispatchers.Main) {
        if(!isBound) {
            val explicitIntent = createExplicitIntent()

            isBound = context.bindService(explicitIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun createExplicitIntent(): Intent {
        val intent = Intent("com.example.aidl.REMOTE_CONNECTION")
        val services = context.packageManager.queryIntentServices(intent, 0)
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

    override fun unbindService() {
        if(isBound) {
            context.unbindService(serviceConnection)
            isBound = false
        }
    }

    override suspend fun deleteDuplicateContacts(): Result<Int> {
        val deferred = CompletableDeferred<Result<Int>>()

        deleteDuplicateContacts?.deleteDuplicateContacts(object : AsyncCallback.Stub() {
            override fun onSuccess(successMessage: Int) {
                deferred.complete(Result.success(successMessage))
            }

            override fun onEmpty(emptyMessage: Int) {
                deferred.complete(Result.success(emptyMessage))
            }

            override fun onError(customAidlException: CustomAidlException?) {
                deferred.complete(Result.failure(RuntimeException(customAidlException?.toException()?.message ?: "Unknown error")))
            }

        })
        return deferred.await()
    }
}