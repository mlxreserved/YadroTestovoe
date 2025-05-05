package com.example.aidl;

import com.example.aidl.CustomAidlException;

// AsyncCallback.aidl
interface AsyncCallback {
    void onSuccess(in String successMessage);
    void onEmpty(in String emptyMessage);
    void onError(in CustomAidlException customAidlException);
}