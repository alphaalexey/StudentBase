package com.daregol.studentbase.ui.students;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

public class StudentsViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final int mGroupId;

    /**
     * Creates a {@code StudentsViewModelFactory}
     *
     * @param application an application to pass in {@link StudentsViewModel}
     */
    public StudentsViewModelFactory(@NonNull Application application, int groupId) {
        mApplication = application;
        mGroupId = groupId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass
                    .getConstructor(Application.class, int.class)
                    .newInstance(mApplication, mGroupId);
        } catch (NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}
