package com.daregol.studentbase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.daregol.studentbase.data.Facility;
import com.daregol.studentbase.data.Group;
import com.daregol.studentbase.data.Student;
import com.daregol.studentbase.db.AppDatabase;

import java.util.List;

public class DataRepository {
    private static volatile DataRepository instance;

    private final AppDatabase database;
    private final MediatorLiveData<List<Facility>> observableFacilities;

    private DataRepository(final AppDatabase database) {
        this.database = database;
        observableFacilities = new MediatorLiveData<>();

        observableFacilities.addSource(this.database.facilityDao().loadAll(),
                facilityEntities -> {
                    if (this.database.getDatabaseCreated().getValue() != null) {
                        observableFacilities.postValue(facilityEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository(database);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Facility>> getFacilities() {
        return observableFacilities;
    }

    public LiveData<List<Group>> loadGroups(final int facilityId) {
        return database.groupDao().loadGroups(facilityId);
    }

    public LiveData<List<Student>> loadStudents(final int groupId) {
        return database.studentDao().loadStudents(groupId);
    }
}
