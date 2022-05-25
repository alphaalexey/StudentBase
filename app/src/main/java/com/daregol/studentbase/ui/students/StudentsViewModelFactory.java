package com.daregol.studentbase.ui.students;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

/**
 * {@link ViewModelProvider.Factory} which may create {@link StudentsViewModel},
 * which has two arguments.
 */
public class StudentsViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final int mGroupId;

    /**
     * Creates a {@code StudentsViewModelFactory}
     *
     * @param application an application to pass in {@link StudentsViewModel}
     * @param groupId     an id of students' group
     */
    public StudentsViewModelFactory(@NonNull Application application, int groupId) {
        mApplication = application;
        mGroupId = groupId;
    }

    /**
     * Creates a new instance of the given {@code Class}.
     * <p>
     *
     * @param modelClass a {@code Class} whose instance is requested
     * @param <T>        The type parameter for the ViewModel.
     * @return a newly created ViewModel
     */
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
