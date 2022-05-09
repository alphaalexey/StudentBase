package com.daregol.studentbase.ui.info;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.daregol.studentbase.R;

public class InfoViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mText;

    public InfoViewModel(@NonNull Application application) {
        super(application);

        mText = new MutableLiveData<>();
        mText.setValue(application.getString(R.string.info));
    }

    public LiveData<String> getText() {
        return mText;
    }
}
