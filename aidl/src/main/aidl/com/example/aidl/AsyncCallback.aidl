package com.example.aidl;

import com.example.aidl.CustomAidlException;

// AsyncCallback.aidl
interface AsyncCallback {
    void onSuccess(in int successMessage);
    void onEmpty(in int emptyMessage);
    void onError(in CustomAidlException customAidlException);
}