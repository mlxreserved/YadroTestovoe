package com.example.aidl;

import com.example.aidl.CustomAidlException;

// AsyncCallback.aidl
interface AsyncCallback {
    void onSuccess(in Int successCode);
    void onEmpty(in String emptyMessage);
    void onError(in CustomAidlException customAidlException);
}