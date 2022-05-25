package com.daregol.studentbase.ui.groups;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

/**
 * {@link ViewModelProvider.Factory} which may create {@link GroupsViewModel},
 * which has two arguments.
 */
public class GroupsViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final int mFacilityId;

    /**
     * Creates a {@code GroupsViewModelFactory}
     *
     * @param application an application to pass in {@link GroupsViewModel}
     * @param facilityId  an id of groups' facility
     */
    public GroupsViewModelFactory(@NonNull Application application, int facilityId) {
        mApplication = application;
        mFacilityId = facilityId;
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
                    .newInstance(mApplication, mFacilityId);
        } catch (NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}
