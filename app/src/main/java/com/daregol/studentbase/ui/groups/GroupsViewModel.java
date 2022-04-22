package com.daregol.studentbase.ui.groups;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.daregol.studentbase.AppExecutors;
import com.daregol.studentbase.DataRepository;
import com.daregol.studentbase.data.Group;
import com.daregol.studentbase.db.AppDatabase;

import java.util.List;

public class GroupsViewModel extends AndroidViewModel {
    private final LiveData<List<Group>> groups;

    public GroupsViewModel(@NonNull Application application, int facilityId) {
        super(application);

        AppExecutors executors = AppExecutors.getInstance();
        AppDatabase database = AppDatabase.getInstance(application, executors);
        DataRepository repository = DataRepository.getInstance(database);

        groups = repository.loadGroups(facilityId);
    }

    public LiveData<List<Group>> getGroups() {
        return groups;
    }
}
