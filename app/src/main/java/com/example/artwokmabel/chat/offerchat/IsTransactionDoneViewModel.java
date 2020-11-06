package com.example.artwokmabel.chat.offerchat;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IsTransactionDoneViewModel extends ViewModel {
    private MutableLiveData<Boolean> isTransactionDone = new MutableLiveData<>(false);

    public IsTransactionDoneViewModel(){

    }

    public MutableLiveData<Boolean> getIsTransactionDone() {
        return isTransactionDone;
    }

    public void setIsTransactionDone(boolean isTransactionDone) {
        this.isTransactionDone.setValue(isTransactionDone);
    }

}
