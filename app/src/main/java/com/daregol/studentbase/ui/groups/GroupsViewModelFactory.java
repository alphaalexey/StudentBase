package com.daregol.studentbase.ui.groups;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

public class GroupsViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final int mFacilityId;

    /**
     * Creates a {@code GroupsViewModelFactory}
     *
     * @param application an application to pass in {@link GroupsViewModel}
     */
    public GroupsViewModelFactory(@NonNull Application application, int facilityId) {
        mApplication = application;
        mFacilityId = facilityId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass
                    .getConstructor(Application.class, int.class)
                    .newInstance(mApplication, mFacilityId);
        } catch (NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}
